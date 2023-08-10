package sori.jakku.kkunkkyu.memore.domain.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ConTagUpdateDto {

    private String keyword;
    private String content;
    private Map<String, Boolean> tag;

}
