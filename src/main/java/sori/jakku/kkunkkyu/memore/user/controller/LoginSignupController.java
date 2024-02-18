package sori.jakku.kkunkkyu.memore.user.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.user.dto.UserDto;
import sori.jakku.kkunkkyu.memore.user.service.UserUseCase;
import sori.jakku.kkunkkyu.memore.web.Response;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginSignupController {

    private final UserUseCase userUseCase;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid UserDto userDto) {
        return userUseCase.login(userDto);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response> signUp(@RequestBody @Valid UserDto userDto) {
        userUseCase.signUp(userDto);

        return Response.ok();
    }

    @PostMapping("/sign-up/username/valid")
    public ResponseEntity<Response> usernameValid(@RequestBody @NonNull String username) {
        boolean valid = false;

        valid = userUseCase.usernameDupl(username);

        if (!valid) {
            return Response.badRequest("NOT_VALID");
        }

        return Response.ok();
    }
}
