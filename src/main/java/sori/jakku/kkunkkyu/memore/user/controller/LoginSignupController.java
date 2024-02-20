package sori.jakku.kkunkkyu.memore.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.user.dto.LoginDto;
import sori.jakku.kkunkkyu.memore.user.dto.SignUpDto;
import sori.jakku.kkunkkyu.memore.user.dto.UsernameValidDto;
import sori.jakku.kkunkkyu.memore.user.service.UserUseCase;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginSignupController {

    private final UserUseCase userUseCase;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid LoginDto dto) {
        return userUseCase.login(dto);
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid SignUpDto dto) {
        userUseCase.signUp(dto);
    }

    @PostMapping("/sign-up/username/valid")
    public void usernameValid(@RequestBody @Valid UsernameValidDto dto) {}
}
