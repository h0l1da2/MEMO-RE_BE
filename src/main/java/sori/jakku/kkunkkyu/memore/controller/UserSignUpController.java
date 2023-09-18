package sori.jakku.kkunkkyu.memore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.UsernameDuplException;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.web.Response;

/**
 * 회원가입
 */
@RestController
@RequiredArgsConstructor
public class UserSignUpController {

    private final UserService userService;
    /**
     * 요청이 3개가 온다.
     * 아이디 중복
     * 패스워드 확인
     * 회원 가입
     */
    @PostMapping("/usernameValid")
    public ResponseEntity<Response> usernameValid(@RequestBody String username) throws UsernameDuplException {
        boolean valid = false;

        valid = userService.usernameDupl(username);

        if (!valid) {
            return Response.badRequest("NOT_VALID");
        }

        return Response.ok();
    }

    @PostMapping("/signUp")
    public ResponseEntity<Response> signUp(@RequestBody @Valid UserDto userDto) throws UsernameDuplException {
        userService.signUp(userDto);

        return Response.ok();
    }

}
