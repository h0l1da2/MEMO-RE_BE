package sori.jakku.kkunkkyu.memore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.domain.dto.TagWriteDto;
import sori.jakku.kkunkkyu.memore.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.service.inter.TagService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;
import sori.jakku.kkunkkyu.memore.web.Response;

import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final WebService webService;

    @GetMapping
    public ResponseEntity<Response> list(HttpServletRequest request) throws UserNotFoundException {
        Long id = webService.getIdInHeader(request);

        List<String> tagList = tagService.tagList(id);
        String tagListJson = webService.objectToJson(tagList);

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
}
