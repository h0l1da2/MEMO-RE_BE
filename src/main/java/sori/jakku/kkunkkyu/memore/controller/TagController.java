package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sori.jakku.kkunkkyu.memore.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.service.inter.TagService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

@Slf4j
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final WebService webService;

    @PostMapping
    public ResponseEntity<String> write(@RequestBody String name, HttpServletRequest request) {

        JsonObject jsonObject = new JsonObject();
        Long id = webService.getIdInHeader(request);

        try {

            String tag = tagService.writeTag(id, name);
            jsonObject.addProperty("name", tag);

        } catch (UserNotFoundException e) {

            log.error("없는 유저.");
            jsonObject.addProperty("response", "USER_NOT_FOUND");
            return webService.badResponse(jsonObject);

        } catch (ConditionNotMatchException e) {

            log.error("태그 양식이 다름.");
            jsonObject.addProperty("response", "NOT_VALID");
            return webService.badResponse(jsonObject);

        }

        return webService.okResponse(jsonObject);
    }
}
