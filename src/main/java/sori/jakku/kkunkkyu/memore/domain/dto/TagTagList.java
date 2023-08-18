package sori.jakku.kkunkkyu.memore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagTagList {
    private String keyword;
    private String content;
    private String name;
}
