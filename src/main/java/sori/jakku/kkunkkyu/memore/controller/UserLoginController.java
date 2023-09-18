package sori.jakku.kkunkkyu.memore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.config.jwt.TokenService;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.web.Response;

import javax.security.auth.login.LoginException;

/**
 * 회원가입
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class UserLoginController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<Response> login(@RequestBody @Valid UserDto userDto) throws LoginException {

        /**
         * 아이디 비밀번호 확인
         * 토큰 생성
         * 세션에 인덱스 등록
         * 토큰 어디에???? ㅇㅅㅇ
         */

        // 아이디 비밀번호 확인
        User user = userService.login(userDto);

        // 토큰 생성
        String token = tokenService.creatToken(user.getId(), user.getUsername());

        return Response.ok(token);
    }
}
