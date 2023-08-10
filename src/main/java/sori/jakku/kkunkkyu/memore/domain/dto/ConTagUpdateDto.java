package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class ConTagUpdateDto {

    @NotBlank @Size(min = 1, max = 20)
    private String keyword;
    private String content;
    private Map<String, Boolean> tag;

}
