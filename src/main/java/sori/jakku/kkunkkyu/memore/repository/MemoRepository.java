package sori.jakku.kkunkkyu.memore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sori.jakku.kkunkkyu.memore.domain.Memo;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query("select m from Memo m join fetch m.user where m.keyword = :keyword")
    Optional<Memo> findByKeyword(String keyword);
}
