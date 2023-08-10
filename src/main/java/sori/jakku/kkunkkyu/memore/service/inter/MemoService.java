package sori.jakku.kkunkkyu.memore.service.inter;

import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;

public interface MemoService {
    void write(Long id, MemoWriteDto memoWriteDto) throws DuplicateMemoException;
}
