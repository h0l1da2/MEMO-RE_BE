package sori.jakku.kkunkkyu.memore.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MainTagSaveDto {

    @NotBlank @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*") @Size(min = 1, max = 20)
    private String tagA;
    @NotBlank @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*") @Size(min = 1, max = 20)
    private String tagB;
    @NotBlank @Pattern(regexp = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*") @Size(min = 1, max = 20)
    private String tagC;

}
