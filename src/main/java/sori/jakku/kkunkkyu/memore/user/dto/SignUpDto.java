package sori.jakku.kkunkkyu.memore.user.dto;

import sori.jakku.kkunkkyu.memore.common.annotation.Password;
import sori.jakku.kkunkkyu.memore.common.annotation.Username;

public record SignUpDto(
        @Username
        String username,
        @Password
        String password

) {

}
