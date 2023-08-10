package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.config.jwt.TokenService;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

import javax.security.auth.login.LoginException;

/**
 * 회원가입
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class UserLoginController {

    private final WebService webService;
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid UserDto userDto, HttpServletRequest request) {

        /**
         * 아이디 비밀번호 확인
         * 토큰 생성
         * 세션에 인덱스 등록
         * 토큰 어디에???? ㅇㅅㅇ
         */

        JsonObject jsonObject = new JsonObject();

        try {
            // 아이디 비밀번호 확인
            User user = userService.login(userDto);

            // 토큰 생성
            String token = tokenService.creatToken(user.getId());
            jsonObject.addProperty("token", token);

        } catch (LoginException e) {
            jsonObject.addProperty("response", "LOGIN_FAIL");
            return webService.badResponse(jsonObject);
        }

        return webService.okResponse(jsonObject);
    }
}
