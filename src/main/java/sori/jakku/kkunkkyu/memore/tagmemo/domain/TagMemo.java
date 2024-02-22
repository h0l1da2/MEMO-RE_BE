package sori.jakku.kkunkkyu.memore.tagmemo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.memo.domain.Memo;
import sori.jakku.kkunkkyu.memore.tag.domain.Tag;

@Entity
@Getter
@NoArgsConstructor
public class TagMemo {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
