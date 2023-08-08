package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagDto {

    @NotBlank
    private String tagA;
    @NotBlank
    private String tagB;
    @NotBlank
    private String tagC;
}
