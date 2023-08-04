package sori.jakku.kkunkkyu.memore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;

/**
 * 로그인, 회원가입
 */
@RestController
public class UserController {

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp() {

        return null;
    }

}
