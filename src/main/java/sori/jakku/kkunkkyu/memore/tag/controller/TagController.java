package sori.jakku.kkunkkyu.memore.tag.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.common.converter.JsonStringConverter;
import sori.jakku.kkunkkyu.memore.tag.dto.TagDto;
import sori.jakku.kkunkkyu.memore.tag.dto.TagWriteDto;
import sori.jakku.kkunkkyu.memore.common.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.common.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.common.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.common.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.tag.service.TagUseCase;
import sori.jakku.kkunkkyu.memore.common.web.WebUseCase;
import sori.jakku.kkunkkyu.memore.web.Response;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagUseCase tagService;
    private final WebUseCase webService;

    @GetMapping
    public ResponseEntity<Response> list(HttpServletRequest request) throws UserNotFoundException {
        Long id = webService.getIdInHeader(request);

        List<String> tagList = tagService.tagList(id);
        String tagListJson = JsonStringConverter.objectToJson(tagList);

        return Response.ok(tagListJson);
    }

    @DeleteMapping
    public ResponseEntity<Response> delete(@RequestBody @Valid TagWriteDto tagWriteDto, HttpServletRequest request) throws UserNotFoundException, MemoNotFoundException {
        Long id = webService.getIdInHeader(request);

        tagService.deleteTag(id, tagWriteDto);

        return Response.ok();
    }

    @PostMapping
    public ResponseEntity<Response> write(@RequestBody @Valid TagWriteDto tagWriteDto, HttpServletRequest request) throws UserNotFoundException, DuplicateMemoException, ConditionNotMatchException {

        Long id = webService.getIdInHeader(request);

        String tag = tagService.writeTag(id, tagWriteDto.getName());

        return Response.ok(tag);
    }

    @PostMapping("/three")
    public ResponseEntity<Response> tagMain(@RequestBody @Valid TagDto tagDto, HttpServletRequest request) throws UserNotFoundException {
        /**
         * 세션에 아이디를 가져와서 태그와 아이디를 넣기
         * 3 개의 태그가 올바른 양식을 가졌는지 확인
         * 태그 값을 응답에 추가 (그래야 본인 페이지로 갔을 때, 그 값들을 보여줄 수 있을듯)
         */
        Long id = webService.getIdInHeader(request);
        String tagJson = tagService.writeForMain(id, tagDto);

        return Response.ok(tagJson);
    }
}
