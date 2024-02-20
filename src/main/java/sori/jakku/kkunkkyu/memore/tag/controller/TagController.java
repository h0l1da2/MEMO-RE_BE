package sori.jakku.kkunkkyu.memore.tag.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.tag.dto.MainTagSaveDto;
import sori.jakku.kkunkkyu.memore.tag.dto.TagDto;
import sori.jakku.kkunkkyu.memore.tag.service.TagUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagUseCase tagService;

    @GetMapping
    public List<String> list(HttpServletRequest request) {
        return tagService.tagList(request);
    }

    @DeleteMapping
    public void delete(@RequestBody @Valid TagDto tagDto, HttpServletRequest request) {
        tagService.deleteTag(request, tagDto);
    }

    @PostMapping
    public void write(@RequestBody @Valid TagDto tagDto, HttpServletRequest request) {
        tagService.writeTag(request, tagDto);
    }

    @PostMapping("/three")
    public MainTagSaveDto tagMain(@RequestBody @Valid MainTagSaveDto mainTagSaveDto, HttpServletRequest request) {
        /**
         * 세션에 아이디를 가져와서 태그와 아이디를 넣기
         * 3 개의 태그가 올바른 양식을 가졌는지 확인
         * 태그 값을 응답에 추가 (그래야 본인 페이지로 갔을 때, 그 값들을 보여줄 수 있을듯)
         */
        return tagService.writeForMain(request, mainTagSaveDto);
    }
}
