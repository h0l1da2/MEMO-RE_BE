package sori.jakku.kkunkkyu.memore.service.inter;

import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.UsernameDuplException;

import javax.security.auth.login.LoginException;

public interface UserService {
    boolean usernameDupl(String username) throws UsernameDuplException;
    // 회원가입
    UserDto signUp(UserDto userDto) throws UsernameDuplException;
    User login(UserDto userDto) throws LoginException;
    User userByUsername(String username);
    User userById(Long id);
}
