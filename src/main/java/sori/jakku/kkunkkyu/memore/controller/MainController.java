package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.domain.dto.Response;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.service.inter.TagService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final TagService tagService;
    private final WebService webService;

    // 메인 페이지는 3개의 태그를 저장한다.
    @PostMapping
    public ResponseEntity<String> tagMain(@RequestBody @Valid TagDto tagDto, HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();

        /**
         * 세션에 아이디를 가져와서 태그와 아이디를 넣기
         * 3 개의 태그가 올바른 양식을 가졌는지 확인
         * 태그 값을 응답에 추가 (그래야 본인 페이지로 갔을 때, 그 값들을 보여줄 수 있을듯)
         */
        Long id = webService.getIdInHeader(request);

        try {

            String tagJson = tagService.writeForMain(id, tagDto);
            jsonObject.addProperty("tag", tagJson);

        } catch (UserNotFoundException e) {

            log.error("헤더에 들어있는 id 가 진짜 id 가 아니었다 Id = {}", id);
            jsonObject.addProperty("response", Response.USER_NOT_FOUND);
            return webService.badResponse(jsonObject);

        }

        return webService.okResponse(jsonObject);
    }

}
