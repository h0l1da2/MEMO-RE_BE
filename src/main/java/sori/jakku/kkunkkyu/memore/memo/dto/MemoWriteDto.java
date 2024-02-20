package sori.jakku.kkunkkyu.memore.memo.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.common.annotation.Memo;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoWriteDto {

    @Memo
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
