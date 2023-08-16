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
    public ResponseEntity<String> list(HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        try {

            List<String> tagList = tagService.tagList(id);
            String tagListJson = webService.objectToJson(tagList);
            jsonObject.addProperty("tagList", tagListJson);

        } catch (UserNotFoundException e) {
            log.error("유저 찾을 수 없음 = {}", id);
            jsonObject.addProperty("response", Response.USER_NOT_FOUND);
            return webService.badResponse(jsonObject);
        }

        return webService.okResponse(jsonObject);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody TagWriteDto tagWriteDto, HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        try {
            tagService.deleteTag(id, tagWriteDto);
        } catch (UserNotFoundException e) {
            log.error("로그인이 안 되어 있음");
            jsonObject.addProperty("response", Response.USER_NOT_FOUND);
            return webService.badResponse(jsonObject);
        } catch (MemoNotFoundException e) {
            log.error("없는 태그거나 이미 지워진 태그");
            jsonObject.addProperty("response", Response.NOT_FOUND);
            return webService.badResponse(jsonObject);
        }

        return webService.okResponse(jsonObject);
    }

    @PostMapping
    public ResponseEntity<String> write(@RequestBody TagWriteDto tagWriteDto, HttpServletRequest request) {

        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        try {
            String tag = tagService.writeTag(id, tagWriteDto.getName());
            jsonObject.addProperty("name", tag);

        } catch (UserNotFoundException e) {

            log.error("없는 유저.");
            jsonObject.addProperty("response", Response.USER_NOT_FOUND);
            return webService.badResponse(jsonObject);

        } catch (DuplicateMemoException e) {

            log.error("태그 중복");
            jsonObject.addProperty("response", Response.DUPLICATE);
            return webService.badResponse(jsonObject);
        }

        return webService.okResponse(jsonObject);
    }
}
