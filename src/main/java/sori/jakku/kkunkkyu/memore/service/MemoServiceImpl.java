package sori.jakku.kkunkkyu.memore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.Memo;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
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
            throw new DuplicateMemoException("중복메모");
        }

        User user = userService.userById(id);
        // 태그 없으면 추가, 불러오기
        tagMemoRepository.saveTagAndMemo(user, memoWriteDto);

    }
}
