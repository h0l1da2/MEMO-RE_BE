package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.domain.User;

@Data
@NoArgsConstructor
public class UserDto {

    @NotBlank @Size(min = 4, max = 15) @Pattern(regexp = "^[a-z0-9]+$")
    private String username;
    @NotBlank @Size(min = 6, max = 15) @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String password;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

}
