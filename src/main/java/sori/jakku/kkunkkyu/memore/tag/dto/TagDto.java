package sori.jakku.kkunkkyu.memore.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TagDto(
        @NotBlank @Size(min = 1, max = 20)
        @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*")
        String name
) {
}
