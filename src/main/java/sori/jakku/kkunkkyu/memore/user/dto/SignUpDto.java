package sori.jakku.kkunkkyu.memore.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpDto(
        @NotBlank @Size(min = 4, max = 15) @Pattern(regexp = "^[a-z0-9]+$")
        String username,
        @NotBlank @Size(min = 6, max = 15) @Pattern(regexp = "^[a-zA-Z0-9]+$")
        String password

) {

}
