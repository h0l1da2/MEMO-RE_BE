package sori.jakku.kkunkkyu.memore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoListDto {
    private String keyword;
    private String content;
    private List<String> tag;

    public MemoListDto(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }
}
