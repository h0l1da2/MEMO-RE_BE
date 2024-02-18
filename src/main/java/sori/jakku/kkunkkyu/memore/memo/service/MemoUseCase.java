package sori.jakku.kkunkkyu.memore.memo.service;

import org.springframework.data.domain.Pageable;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoWriteDto;

import java.util.List;

public interface MemoUseCase {
    void write(Long id, MemoWriteDto memoWriteDto);
    void changeMemo(Long id, MemoUpdateDto memoUpdateDto);
    void removeMemo(Long id, String keyword);
    List<MemoListDto> memoList(Long id, Pageable pageable, String tag);
}
