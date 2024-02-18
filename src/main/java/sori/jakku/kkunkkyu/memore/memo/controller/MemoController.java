package sori.jakku.kkunkkyu.memore.memo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.common.converter.JsonStringConverter;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.memo.service.MemoUseCase;
import sori.jakku.kkunkkyu.memore.common.web.WebUseCase;
import sori.jakku.kkunkkyu.memore.web.Response;

import java.util.List;

@RestController
@RequestMapping("/api/v1/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoUseCase memoService;
    private final WebUseCase webService;

    @GetMapping
    public ResponseEntity<Response> list(@RequestParam(required = false) String tag, @PageableDefault(size = 12) Pageable pageable, HttpServletRequest request) {

        Long id = webService.getIdInHeader(request);
        List<MemoListDto> memoList = memoService.memoList(id, pageable, tag);

        String memoListJson = JsonStringConverter.objectToJson(memoList);

        return Response.ok(memoListJson);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteMemo(@RequestBody String keyword, HttpServletRequest request) {
        Long id = webService.getIdInHeader(request);

        String key = JsonStringConverter.jsonToString(keyword, "keyword");
        memoService.removeMemo(id, key);

        return Response.ok();
    }

    @PutMapping
    public ResponseEntity<Response> changeMemo(@RequestBody @Valid MemoUpdateDto memoUpdateDto, HttpServletRequest request) {
        /**
         * 원래 키워드 확인 후,
         * 있다면 전부 수정
         * 태그테이블도 삭제
         */
        Long id = webService.getIdInHeader(request);

        memoService.changeMemo(id, memoUpdateDto);

        return Response.ok();
    }

    @PostMapping
    public ResponseEntity<Response> write(@RequestBody @Valid MemoWriteDto memoWriteDto, HttpServletRequest request) {

        Long id = webService.getIdInHeader(request);

        memoService.write(id, memoWriteDto);

        return Response.ok();
    }

}
