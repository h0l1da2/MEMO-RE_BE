package sori.jakku.kkunkkyu.memore.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.common.config.jwt.TokenUseCase;
import sori.jakku.kkunkkyu.memore.common.exception.UsernameDuplException;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.user.dto.UserDto;
import sori.jakku.kkunkkyu.memore.user.service.UserUseCase;
import sori.jakku.kkunkkyu.memore.web.Response;

import javax.security.auth.login.LoginException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginSignupController {

    private final UserUseCase userService;
    private final TokenUseCase tokenService;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody @Valid UserDto userDto) throws LoginException {
        // 아이디 비밀번호 확인
        User user = userService.login(userDto);

        // 토큰 생성
        String token = tokenService.creatToken(user.getId(), user.getUsername());

        return Response.ok(token);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response> signUp(@RequestBody @Valid UserDto userDto) throws UsernameDuplException {
        userService.signUp(userDto);

        return Response.ok();
    }

    @PostMapping("/sign-up/username/valid")
    public ResponseEntity<Response> usernameValid(@RequestBody String username) throws UsernameDuplException {
        boolean valid = false;

        valid = userService.usernameDupl(username);

        if (!valid) {
            return Response.badRequest("NOT_VALID");
        }

        return Response.ok();
    }
}
