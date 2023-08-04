package sori.jakku.kkunkkyu.memore.service;

import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;

public interface UserServiceImpl {
    // 회원가입
    UserDto signUp(UserDto userDto);
}
