package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.config.jwt.TokenService;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

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
    public ResponseEntity<String> login(@RequestBody @Valid UserDto userDto) {

        /**
         * 아이디 비밀번호 확인
         * 토큰 생성
         * 세션에 인덱스 등록
         * 토큰 어디에???? ㅇㅅㅇ
         */

        JsonObject jsonObject = new JsonObject();

        // 아이디 비밀번호 확인
        boolean loginValid = userService.login(userDto);
        if (!loginValid) {
            jsonObject.addProperty("response", "LOGIN_FAIL");
            return webService.badResponse(jsonObject);
        }

        // 토큰 생성
        String token = tokenService.creatToken(userDto.getUsername());
        jsonObject.addProperty("token", token);

        jsonObject.addProperty("response", "OK");
        return webService.okResponse(jsonObject);
    }
}
