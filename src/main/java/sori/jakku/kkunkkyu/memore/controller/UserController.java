package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.NotValidException;
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
        JsonObject jsonObject = new JsonObject();
        if (!valid) {
            jsonObject.addProperty("data", "NOT_VALID");
            return webService.badResponse(jsonObject);
        }
        jsonObject.addProperty("data", "OK_VALID");
        return webService.okResponse(jsonObject);
    }

    @PostMapping("/pwdValid")
    public ResponseEntity<UserDto> pwdValid(@Valid UserDto userDto) {
        return null;
    }
    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@Valid UserDto userDto) throws NotValidException {

        UserDto signUser = userService.signUp(userDto);

        return null;
    }

}
