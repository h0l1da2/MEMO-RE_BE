package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.domain.User;

@Data
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

}
