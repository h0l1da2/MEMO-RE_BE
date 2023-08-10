package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MemoWriteDto {

    @NotBlank @Size(min = 1, max = 20)
    private String keyword;
    @Size(max = 100)
    private String content;
    @Size(min = 1, max = 10)
    private List<String> tag;

}
