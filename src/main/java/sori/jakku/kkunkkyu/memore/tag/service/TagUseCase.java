package sori.jakku.kkunkkyu.memore.tag.service;

import sori.jakku.kkunkkyu.memore.tag.dto.TagDto;
import sori.jakku.kkunkkyu.memore.tag.dto.TagWriteDto;
import sori.jakku.kkunkkyu.memore.common.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.common.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.common.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.common.exception.UserNotFoundException;

import java.util.List;

public interface TagUseCase {
    String writeForMain(Long id, TagDto tagDto) throws UserNotFoundException;
    String writeTag(Long id, String name) throws UserNotFoundException, DuplicateMemoException, ConditionNotMatchException;
    void deleteTag(Long id, TagWriteDto tagWriteDto) throws UserNotFoundException, MemoNotFoundException;
    List<String> tagList(Long id) throws UserNotFoundException;
}
