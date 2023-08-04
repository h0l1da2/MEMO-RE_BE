package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.PasswordNotValidException;
import sori.jakku.kkunkkyu.memore.exception.UsernameNotValidException;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

/**
 * 로그인, 회원가입
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WebService webService;
    /**
     * 요청이 3개가 온다.
     * 아이디 중복
     * 패스워드 확인
     * 회원 가입
     */
    @PostMapping("/usernameValid")
    public ResponseEntity<String> usernameValid(String username) {
        boolean valid = userService.usernameDupl(username);
        return validResponse(valid);
    }

    @PostMapping("/pwdValid")
    public ResponseEntity<String> pwdValid(String password) {
        boolean valid = userService.pwdValid(password);
        return validResponse(valid);
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@Valid UserDto userDto) {
        JsonObject jsonObject = new JsonObject();
        try {

            userService.signUp(userDto);

        } catch (UsernameNotValidException e) {

            jsonObject.addProperty("data", "BAD_USERNAME");
            return webService.badResponse(jsonObject);

        } catch (PasswordNotValidException e) {

            jsonObject.addProperty("data", "BAD_PWD");
            return webService.badResponse(jsonObject);

        }

        jsonObject.addProperty("data", "OK");
        return webService.okResponse(jsonObject);
    }

    private ResponseEntity<String> validResponse(boolean valid) {
        JsonObject jsonObject = new JsonObject();
        if (!valid) {
            jsonObject.addProperty("data", "NOT_VALID");
            return webService.badResponse(jsonObject);
        }
        jsonObject.addProperty("data", "OK");
        return webService.okResponse(jsonObject);
    }
}
