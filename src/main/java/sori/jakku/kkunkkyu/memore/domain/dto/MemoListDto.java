package sori.jakku.kkunkkyu.memore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoListDto {
    private String keyword;
    private String content;
    @Builder.Default
    private List<String> tag = new ArrayList<>();

    public MemoListDto(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }
}
