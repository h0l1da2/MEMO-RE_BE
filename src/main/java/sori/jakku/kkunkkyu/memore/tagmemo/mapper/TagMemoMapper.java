package sori.jakku.kkunkkyu.memore.tagmemo.mapper;

import org.mapstruct.Mapper;
import sori.jakku.kkunkkyu.memore.memo.domain.Memo;
import sori.jakku.kkunkkyu.memore.tag.domain.Tag;
import sori.jakku.kkunkkyu.memore.tagmemo.domain.TagMemo;

@Mapper
public interface TagMemoMapper {
    TagMemo toEntity(Memo memo, Tag tag);
}
