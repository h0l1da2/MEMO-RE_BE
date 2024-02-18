package sori.jakku.kkunkkyu.memore.user.service;

import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.user.dto.UserDto;
import sori.jakku.kkunkkyu.memore.common.exception.UsernameDuplException;

import javax.security.auth.login.LoginException;
import java.util.Map;

public interface UserUseCase {
    boolean usernameDupl(String username);
    // 회원가입
    UserDto signUp(UserDto userDto);
    Map<String, String> login(UserDto userDto);
    User userById(Long id);
}
