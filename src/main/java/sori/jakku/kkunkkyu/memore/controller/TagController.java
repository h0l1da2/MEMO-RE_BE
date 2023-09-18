package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.domain.dto.Response;
import sori.jakku.kkunkkyu.memore.domain.dto.TagWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.service.inter.TagService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final WebService webService;

    @GetMapping
    public ResponseEntity<String> list(HttpServletRequest request) throws UserNotFoundException {
        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        List<String> tagList = tagService.tagList(id);
        String tagListJson = webService.objectToJson(tagList);
        jsonObject.addProperty("tagList", tagListJson);


        return webService.okResponse(jsonObject);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody TagWriteDto tagWriteDto, HttpServletRequest request) throws UserNotFoundException, MemoNotFoundException {
        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        tagService.deleteTag(id, tagWriteDto);

        return webService.okResponse(jsonObject);
    }

    @PostMapping
    public ResponseEntity<String> write(@RequestBody TagWriteDto tagWriteDto, HttpServletRequest request) throws UserNotFoundException, DuplicateMemoException {

        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        String tag = tagService.writeTag(id, tagWriteDto.getName());
        jsonObject.addProperty("name", tag);


        return webService.okResponse(jsonObject);
    }
}
