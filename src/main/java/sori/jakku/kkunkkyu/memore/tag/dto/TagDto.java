package sori.jakku.kkunkkyu.memore.tag.dto;

import sori.jakku.kkunkkyu.memore.common.annotation.Tag;

public record TagDto(
        @Tag
        String name
) {
}
