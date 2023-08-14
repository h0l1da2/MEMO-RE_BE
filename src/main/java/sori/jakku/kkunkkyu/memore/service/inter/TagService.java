package sori.jakku.kkunkkyu.memore.service.inter;

import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;
import sori.jakku.kkunkkyu.memore.domain.dto.TagWriteDto;
import sori.jakku.kkunkkyu.memore.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;

public interface TagService {
    String writeForMain(Long id, TagDto tagDto) throws UserNotFoundException, ConditionNotMatchException;
    String writeTag(Long id, String name) throws UserNotFoundException, ConditionNotMatchException, DuplicateMemoException;
    void deleteTag(Long id, TagWriteDto tagWriteDto) throws UserNotFoundException, MemoNotFoundException;
}
