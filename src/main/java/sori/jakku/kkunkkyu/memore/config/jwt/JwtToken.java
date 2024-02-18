package sori.jakku.kkunkkyu.memore.config.jwt;

import lombok.Getter;

@Getter
public enum JwtToken {

    TOKEN("token"),

    ACCESS_TOKEN("access_token"),

    TYP("typ"),
    BEARER("Bearer"),

    ID("id"),
    USERNAME("username"),
    ROLE("role"),
    ROLE_USER("[ROLE_USER]")
    ;

    private final String value;

    JwtToken(String value) {
        this.value = value;
    }
}
