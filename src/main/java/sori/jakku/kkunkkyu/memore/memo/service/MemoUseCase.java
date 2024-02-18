package sori.jakku.kkunkkyu.memore.memo.service;

import org.springframework.data.domain.Pageable;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.common.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.common.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.common.exception.UserNotFoundException;

import java.util.List;

public interface MemoUseCase {
    void write(Long id, MemoWriteDto memoWriteDto) throws DuplicateMemoException, UserNotFoundException;
    void changeMemo(Long id, MemoUpdateDto memoUpdateDto) throws MemoNotFoundException, UserNotFoundException, DuplicateMemoException;
    void removeMemo(Long id, String keyword) throws MemoNotFoundException, UserNotFoundException;
    List<MemoListDto> memoList(Long id, Pageable pageable, String tag);
}
