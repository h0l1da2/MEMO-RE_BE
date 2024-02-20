package sori.jakku.kkunkkyu.memore.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.common.annotation.Memo;

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

    public MemoListDto(@Memo String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }
}
