package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoUpdateDto {

    @NotBlank @Size(min = 1, max = 20)
    private String originKey;
    @NotBlank @Size(min = 1, max = 20)
    private String newKey;
    private String content;
    private Map<String, Boolean> tag;

}
