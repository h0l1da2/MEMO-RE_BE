package sori.jakku.kkunkkyu.memore.service.inter;

import sori.jakku.kkunkkyu.memore.domain.dto.ConTagUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;

public interface MemoService {
    void write(Long id, MemoWriteDto memoWriteDto) throws DuplicateMemoException;
    void changeContentTag(Long id, ConTagUpdateDto conTagUpdateDto) throws MemoNotFoundException, UserNotFoundException;
    void removeMemo(Long id, String keyword) throws MemoNotFoundException, UserNotFoundException;
}
