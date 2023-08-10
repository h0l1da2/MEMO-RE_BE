package sori.jakku.kkunkkyu.memore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sori.jakku.kkunkkyu.memore.domain.Memo;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    Optional<Memo> findByKeyword(String keyword);
}
