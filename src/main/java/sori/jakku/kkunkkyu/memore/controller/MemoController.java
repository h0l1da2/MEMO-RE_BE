package sori.jakku.kkunkkyu.memore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;
import sori.jakku.kkunkkyu.memore.web.Response;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final WebService webService;

    @GetMapping
    public ResponseEntity<Response> list(@RequestParam(required = false) String tag, @PageableDefault(size = 12) Pageable pageable, HttpServletRequest request) {

        Long id = webService.getIdInHeader(request);
        List<MemoListDto> memoList = memoService.memoList(id, pageable, tag);

        String memoListJson = webService.objectToJson(memoList);

        return Response.ok(memoListJson);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteMemo(@RequestBody String keyword, HttpServletRequest request) throws UserNotFoundException, MemoNotFoundException {
        Long id = webService.getIdInHeader(request);

        String key = webService.jsonToString(keyword, "keyword");
        memoService.removeMemo(id, key);

        return Response.ok();
    }

    @PutMapping
    public ResponseEntity<Response> changeMemo(@RequestBody @Valid MemoUpdateDto memoUpdateDto, HttpServletRequest request) throws UserNotFoundException, DuplicateMemoException, MemoNotFoundException {
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
    public ResponseEntity<Response> write(@RequestBody @Valid MemoWriteDto memoWriteDto, HttpServletRequest request) throws UserNotFoundException, DuplicateMemoException {

        Long id = webService.getIdInHeader(request);

        memoService.write(id, memoWriteDto);

        return Response.ok();
    }

}
