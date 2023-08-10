package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class KeywordDto {
    @NotBlank @Size(min = 1, max = 20)
    private String originKey;
    @NotBlank @Size(min = 1, max = 20)
    private String newKey;
}
