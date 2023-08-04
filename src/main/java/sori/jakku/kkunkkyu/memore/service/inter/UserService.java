package sori.jakku.kkunkkyu.memore.service.inter;

import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;

public interface UserService {
    // 회원가입
    UserDto signUp(UserDto userDto);
}
