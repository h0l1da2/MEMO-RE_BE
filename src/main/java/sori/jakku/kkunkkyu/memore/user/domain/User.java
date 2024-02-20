package sori.jakku.kkunkkyu.memore.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.user.dto.LoginDto;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(LoginDto loginDto) {
        this.username = loginDto.username();
        this.password = loginDto.password();
    }

    public void encodedPassword(String encodePwd) {
        this.password = encodePwd;
    }
}
