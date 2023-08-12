package sori.jakku.kkunkkyu.memore.domain;

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
}
