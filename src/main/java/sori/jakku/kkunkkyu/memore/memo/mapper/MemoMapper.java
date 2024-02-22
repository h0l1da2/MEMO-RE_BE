package sori.jakku.kkunkkyu.memore.memo.mapper;

import org.mapstruct.Mapper;
import sori.jakku.kkunkkyu.memore.memo.domain.Memo;
import sori.jakku.kkunkkyu.memore.user.domain.User;

@Mapper
public interface MemoMapper {
    Memo toEntity(User user, String keyword);
}
