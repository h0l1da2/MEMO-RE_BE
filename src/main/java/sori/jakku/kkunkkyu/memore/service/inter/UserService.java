package sori.jakku.kkunkkyu.memore.service.inter;

import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.PasswordNotValidException;
import sori.jakku.kkunkkyu.memore.exception.UsernameNotValidException;

public interface UserService {
    boolean usernameDupl(String username);
    boolean pwdValid(String password);
    // 회원가입
    UserDto signUp(UserDto userDto) throws UsernameNotValidException, PasswordNotValidException;
}
