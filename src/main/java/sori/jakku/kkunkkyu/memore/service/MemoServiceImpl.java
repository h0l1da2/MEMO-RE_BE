package sori.jakku.kkunkkyu.memore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.Memo;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.repository.CustomTagMemoRepository;
import sori.jakku.kkunkkyu.memore.repository.MemoRepository;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {

    private final MemoRepository memoRepository;
    private final UserService userService;
    private final CustomTagMemoRepository tagMemoRepository;


    @Override
    public void write(Long id, MemoWriteDto memoWriteDto) throws DuplicateMemoException, UserNotFoundException {
        User user = userService.userById(id);

        if (user == null) {
            throw new UserNotFoundException();
        }

        // 키워드 중복 조사
        Memo findMemo = memoRepository.findByKeywordAndUser(memoWriteDto.getKeyword(), user).orElse(null);

        if (findMemo != null) {
            throw new DuplicateMemoException("중복 메모");
        }

        // 태그 없으면 추가, 불러오기
        tagMemoRepository.saveTagAndMemo(user, memoWriteDto);

    }

    @Override
    public void changeMemo(Long id, MemoUpdateDto memoUpdateDto) throws MemoNotFoundException, UserNotFoundException, DuplicateMemoException {
        User user = userService.userById(id);

        Memo memo = memoRepository.findByKeywordAndUser(memoUpdateDto.getOriginKey(), user)
                .orElseThrow(MemoNotFoundException::new);

        if (memo.getUser() != user) {
            throw new UserNotFoundException("본인 메모가 아닙니다.");
        }

        memoRepository.findByKeywordAndUser(memoUpdateDto.getNewKey(), user)
                .orElseThrow(DuplicateMemoException::new);
        tagMemoRepository.updateMemoAndTag(memo, memoUpdateDto);

    }

    @Override
    public void removeMemo(Long id, String keyword) throws MemoNotFoundException, UserNotFoundException {
        User user = userService.userById(id);

        Memo memo = memoRepository.findByKeywordAndUser(keyword, user)
                .orElseThrow(MemoNotFoundException::new);

        if (memo.getUser() != user) {
            throw new UserNotFoundException("본인 메모가 아닙니다.");
        }
        // 메모태그 레코드 다 지우고
        tagMemoRepository.deleteMemo(memo);

    }

    @Override
    public List<MemoListDto> memoList(Long id, Pageable pageable, String tag) {
        /**
         * 메모 가져오기
         * 그 메모로 태그메모 싹 가져오기
         * 태그메모로 태그 가져오기
         */
        // 페이지에 맞는 메모 다 가져오기
        return tagMemoRepository.findAllForList(id, pageable, tag);
    }
}
