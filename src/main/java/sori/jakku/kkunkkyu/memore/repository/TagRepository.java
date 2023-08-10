package sori.jakku.kkunkkyu.memore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sori.jakku.kkunkkyu.memore.domain.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
