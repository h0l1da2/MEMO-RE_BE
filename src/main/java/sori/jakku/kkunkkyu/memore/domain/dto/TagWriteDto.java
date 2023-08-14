package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagWriteDto {

    @NotBlank @Size(min = 1, max = 20)
    @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*")
    private String name;
}
