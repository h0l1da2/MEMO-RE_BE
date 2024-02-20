package sori.jakku.kkunkkyu.memore.user.dto;

import sori.jakku.kkunkkyu.memore.common.annotation.Username;

public record UsernameValidDto(
        @Username
        String username
){}
