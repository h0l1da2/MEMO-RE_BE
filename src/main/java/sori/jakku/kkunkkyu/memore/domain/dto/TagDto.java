package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagDto {

    @NotBlank @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*") @Size(min = 1, max = 20)
    private String tagA;
    @NotBlank @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*") @Size(min = 1, max = 20)
    private String tagB;
    @NotBlank @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*") @Size(min = 1, max = 20)
    private String tagC;

    public TagDto() {
    }

    public TagDto(String tagA, String tagB, String tagC) {
        this.tagA = tagA;
        this.tagB = tagB;
        this.tagC = tagC;
    }
}
