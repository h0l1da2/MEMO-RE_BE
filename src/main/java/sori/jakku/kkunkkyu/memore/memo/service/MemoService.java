package sori.jakku.kkunkkyu.memore.memo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.common.exception.*;
import sori.jakku.kkunkkyu.memore.common.exception.Exception;
import sori.jakku.kkunkkyu.memore.memo.domain.Memo;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.tagmemo.repository.CustomTagMemoRepository;
import sori.jakku.kkunkkyu.memore.memo.repository.MemoRepository;
import sori.jakku.kkunkkyu.memore.user.service.UserUseCase;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoService implements MemoUseCase {

    private final MemoRepository memoRepository;
    private final UserUseCase userService;
    private final CustomTagMemoRepository tagMemoRepository;


    @Override
    public void write(Long id, MemoWriteDto memoWriteDto) {
        User user = userService.findById(id);

        if (user == null) {
            log.error("유저 찾을 수 없음 = {}", id);
            throw new BadRequestException(Exception.USER_NOT_FOUND);
        }

        // 키워드 중복 조사
        Memo findMemo = memoRepository.findByKeywordAndUser(memoWriteDto.getKeyword(), user).orElse(null);

        if (findMemo != null) {
            log.error("메모가 중복 = {}", memoWriteDto.getKeyword());
            throw new BadRequestException(Exception.DUPLICATED_MEMO);
        }

        // 태그 없으면 추가, 불러오기
        tagMemoRepository.saveTagAndMemo(user, memoWriteDto);

    }

    @Override
    public void changeMemo(Long id, MemoUpdateDto memoUpdateDto) {
        User user = userService.findById(id);

        Memo memo = memoRepository.findByKeywordAndUser(memoUpdateDto.getOriginKey(), user)
                .orElseThrow(() -> new BadRequestException(Exception.MEMO_NOT_FOUND));

        if (memo.getUser() != user) {
            log.error("본인 메모가 아님 메모 아이디 = {}, 로그인 아이디 = {}", memo.getUser().getId(), id);
            throw new BadRequestException(Exception.NOT_YOUR_DATA);
        }

        Memo findNewKey = memoRepository.findByKeywordAndUser(memoUpdateDto.getNewKey(), user).orElse(null);

        if (findNewKey != null) {
            log.error("메모 키워드가 중복 = {}", findNewKey.getKeyword());
            throw new BadRequestException(Exception.MEMO_NOT_FOUND);
        }

        tagMemoRepository.updateMemoAndTag(memo, memoUpdateDto);

    }

    @Override
    public void removeMemo(Long id, String keyword) {
        User user = userService.findById(id);

        Memo memo = memoRepository.findByKeywordAndUser(keyword, user)
                .orElseThrow(() -> new BadRequestException(Exception.MEMO_NOT_FOUND));

        if (memo.getUser() != user) {
            log.error("본인 메모가 아닙니다. 메모 유저 = {}, 로그인 유저 = {}", user.getId(), id);
            throw new BadRequestException(Exception.NOT_YOUR_DATA);
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
