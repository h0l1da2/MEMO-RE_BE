package sori.jakku.kkunkkyu.memore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoWriteDto {

    @NotBlank @Size(min = 1, max = 20)
    private String keyword;
    @Size(max = 100)
    private String content;
    @Size(max = 10)
    private List<String> tag;

    public MemoWriteDto(String keyword) {
        this.keyword = keyword;
    }
    public MemoWriteDto(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }
    public MemoWriteDto(String keyword, List<String> tag) {
        this.keyword = keyword;
        this.tag = tag;
    }

}
