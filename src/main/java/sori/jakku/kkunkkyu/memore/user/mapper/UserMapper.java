package sori.jakku.kkunkkyu.memore.user.mapper;

import org.mapstruct.Mapper;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.user.dto.SignUpDto;

@Mapper
public interface UserMapper {
    User toEntity(SignUpDto dto);
}
