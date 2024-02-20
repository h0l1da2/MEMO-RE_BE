package sori.jakku.kkunkkyu.memore.tag.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.common.annotation.Tag;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MainTagSaveDto {

    @Tag
    private String tagA;
    @Tag
    private String tagB;
    @Tag
    private String tagC;

}
