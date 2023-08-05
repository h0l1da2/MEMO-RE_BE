package sori.jakku.kkunkkyu.memore.service.inter;

import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.PasswordNotValidException;
import sori.jakku.kkunkkyu.memore.exception.UsernameDuplException;
import sori.jakku.kkunkkyu.memore.exception.UsernameNotValidException;

public interface UserService {
    boolean usernameDupl(String username) throws UsernameDuplException;
    boolean pwdCheck(String password);
    // 회원가입
    UserDto signUp(UserDto userDto) throws UsernameNotValidException, PasswordNotValidException, UsernameDuplException;
    boolean login(UserDto userDto);
    User getUserByUsername(String username);
}
