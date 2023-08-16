package sori.jakku.kkunkkyu.memore.service.inter;

import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;
import sori.jakku.kkunkkyu.memore.domain.dto.TagWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;

import java.util.List;

public interface TagService {
    String writeForMain(Long id, TagDto tagDto) throws UserNotFoundException;
    String writeTag(Long id, String name) throws UserNotFoundException, DuplicateMemoException;
    void deleteTag(Long id, TagWriteDto tagWriteDto) throws UserNotFoundException, MemoNotFoundException;
    List<String> tagList(Long id) throws UserNotFoundException;
}
