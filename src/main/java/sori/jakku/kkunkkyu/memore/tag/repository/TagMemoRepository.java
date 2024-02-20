package sori.jakku.kkunkkyu.memore.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sori.jakku.kkunkkyu.memore.tagmemo.domain.TagMemo;

public interface TagMemoRepository extends JpaRepository<TagMemo, Long> {
}
