package sori.jakku.kkunkkyu.memore.user.service;

import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.user.dto.LoginDto;
import sori.jakku.kkunkkyu.memore.user.dto.SignUpDto;

import java.util.Map;

public interface UserUseCase {
    // 회원가입
    void signUp(SignUpDto signUpDto);
    Map<String, String> login(LoginDto loginDto);
    User findById(Long id);
}
