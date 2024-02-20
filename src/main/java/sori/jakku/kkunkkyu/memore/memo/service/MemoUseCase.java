package sori.jakku.kkunkkyu.memore.memo.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoWriteDto;

import java.util.List;

public interface MemoUseCase {
    void write(HttpServletRequest request, MemoWriteDto memoWriteDto);
    void changeMemo(HttpServletRequest request, MemoUpdateDto memoUpdateDto);
    void removeMemo(HttpServletRequest request, String keyword);
    List<MemoListDto> memoList(HttpServletRequest request, Pageable pageable, String tag);
}
