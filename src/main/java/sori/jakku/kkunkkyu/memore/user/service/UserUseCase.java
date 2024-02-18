package sori.jakku.kkunkkyu.memore.user.service;

import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.user.dto.UserDto;
import sori.jakku.kkunkkyu.memore.common.exception.UsernameDuplException;

import javax.security.auth.login.LoginException;

public interface UserUseCase {
    boolean usernameDupl(String username) throws UsernameDuplException;
    // 회원가입
    UserDto signUp(UserDto userDto) throws UsernameDuplException;
    User login(UserDto userDto) throws LoginException;
    User userByUsername(String username);
    User userById(Long id);
}
