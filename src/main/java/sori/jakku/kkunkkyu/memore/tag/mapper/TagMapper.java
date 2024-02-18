package sori.jakku.kkunkkyu.memore.tag.mapper;

import org.mapstruct.Mapper;
import sori.jakku.kkunkkyu.memore.tag.domain.Tag;
import sori.jakku.kkunkkyu.memore.tag.dto.TagDto;
import sori.jakku.kkunkkyu.memore.user.domain.User;

@Mapper
public interface TagMapper {

    default Tag toEntity(User user, TagDto dto) {
        return Tag.builder()
                .name(dto.getName())
                .user(user)
                .build();
    }

    default Tag toEntity(User user, String name) {
        return Tag.builder()
                .name(name)
                .user(user)
                .build();
    }
}
