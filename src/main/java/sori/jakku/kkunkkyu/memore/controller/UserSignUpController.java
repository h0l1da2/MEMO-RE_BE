package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.domain.dto.Response;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.UsernameDuplException;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

/**
 * 회원가입
 */
@RestController
@RequiredArgsConstructor
public class UserSignUpController {

    private final UserService userService;
    private final WebService webService;
    /**
     * 요청이 3개가 온다.
     * 아이디 중복
     * 패스워드 확인
     * 회원 가입
     */
    @PostMapping("/usernameValid")
    public ResponseEntity<String> usernameValid(@RequestBody String username) {
        boolean valid = false;
        JsonObject jsonObject = new JsonObject();
        try {
            valid = userService.usernameDupl(username);
        } catch (UsernameDuplException e) {
            jsonObject.addProperty("response", Response.USERNAME_DUPL);
            return webService.badResponse(jsonObject);
        }
        return validResponse(valid);
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody @Valid UserDto userDto) {
        JsonObject jsonObject = new JsonObject();
        try {

            userService.signUp(userDto);

        } catch (UsernameDuplException e) {

            jsonObject.addProperty("response", Response.USERNAME_DUPL);
            return webService.badResponse(jsonObject);

        }

        return webService.okResponse(jsonObject);
    }

    private ResponseEntity<String> validResponse(boolean valid) {
        JsonObject jsonObject = new JsonObject();
        if (!valid) {
            jsonObject.addProperty("response", Response.NOT_VALID);
            return webService.badResponse(jsonObject);
        }
        return webService.okResponse(jsonObject);
    }
}
