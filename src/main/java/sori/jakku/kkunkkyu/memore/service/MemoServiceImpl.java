package sori.jakku.kkunkkyu.memore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.Memo;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.ConTagUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.KeywordDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.repository.CustomTagMemoRepository;
import sori.jakku.kkunkkyu.memore.repository.MemoRepository;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {

    private final MemoRepository memoRepository;
    private final UserService userService;
    private final CustomTagMemoRepository tagMemoRepository;


    @Override
    public void write(Long id, MemoWriteDto memoWriteDto) throws DuplicateMemoException {

        // 키워드 중복 조사
        Memo findMemo = memoRepository.findByKeyword(memoWriteDto.getKeyword()).orElse(null);

        if (findMemo != null) {
            throw new DuplicateMemoException("중복 메모");
        }

        User user = userService.userById(id);
        // 태그 없으면 추가, 불러오기
        tagMemoRepository.saveTagAndMemo(user, memoWriteDto);

    }

    @Override
    public void changeKeyword(Long id, KeywordDto keywordDto) throws MemoNotFoundException, UserNotFoundException {
        User user = userService.userById(id);

        Memo memo = memoRepository.findByKeyword(keywordDto.getOriginKey()).orElseThrow(MemoNotFoundException::new);

        if (memo.getUser() != user) {
            throw new UserNotFoundException("본인 메모가 아닙니다.");
        }

        memo.changeKeyword(keywordDto.getNewKey());
        memoRepository.save(memo);

    }

    @Override
    public void changeContentTag(Long id, ConTagUpdateDto conTagUpdateDto) throws MemoNotFoundException, UserNotFoundException {
        User user = userService.userById(id);

        Memo memo = memoRepository.findByKeyword(conTagUpdateDto.getKeyword()).orElseThrow(MemoNotFoundException::new);

        if (memo.getUser() != user) {
            throw new UserNotFoundException("본인 메모가 아닙니다.");
        }

        tagMemoRepository.updateMemoAndTag(memo, conTagUpdateDto);
    }

    @Override
    public void removeMemo(Long id, String keyword) throws MemoNotFoundException, UserNotFoundException {
        User user = userService.userById(id);

        Memo memo = memoRepository.findByKeyword(keyword).orElseThrow(MemoNotFoundException::new);

        if (memo.getUser() != user) {
            throw new UserNotFoundException("본인 메모가 아닙니다.");
        }
        // 메모태그 레코드 다 지우고
        tagMemoRepository.deleteMemo(memo);

    }
}
