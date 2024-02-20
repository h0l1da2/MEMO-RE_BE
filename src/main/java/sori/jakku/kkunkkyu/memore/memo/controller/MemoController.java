package sori.jakku.kkunkkyu.memore.memo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.memo.service.MemoUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/v1/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoUseCase memoService;

    @GetMapping
    public List<MemoListDto> list(@RequestParam(required = false) String tag, @PageableDefault(size = 12) Pageable pageable, HttpServletRequest request) {
        return memoService.memoList(request, pageable, tag);
    }

    @DeleteMapping
    public void deleteMemo(@RequestBody String keyword, HttpServletRequest request) {
        memoService.removeMemo(request, keyword);
    }

    @PutMapping
    public void changeMemo(@RequestBody @Valid MemoUpdateDto memoUpdateDto, HttpServletRequest request) {
        memoService.changeMemo(request, memoUpdateDto);
    }

    @PostMapping
    public void write(@RequestBody @Valid MemoWriteDto memoWriteDto, HttpServletRequest request) {
        memoService.write(request, memoWriteDto);
    }

}
