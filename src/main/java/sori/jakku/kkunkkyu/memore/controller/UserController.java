package sori.jakku.kkunkkyu.memore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.NotValidException;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;

/**
 * 로그인, 회원가입
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 요청이 3개가 온다.
     * 아이디 중복
     * 패스워드 확인
     * 회원 가입
     */
    @PostMapping("/usernameValid")
    public ResponseEntity<UserDto> usernameValid(@Valid UserDto userDto) {
        return null;
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
