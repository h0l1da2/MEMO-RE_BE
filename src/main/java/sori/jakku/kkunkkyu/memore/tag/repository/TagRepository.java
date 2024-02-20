package sori.jakku.kkunkkyu.memore.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sori.jakku.kkunkkyu.memore.tag.domain.Tag;
import sori.jakku.kkunkkyu.memore.user.domain.User;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameAndUser(String name, User user);
    void deleteByName(String name);
}
