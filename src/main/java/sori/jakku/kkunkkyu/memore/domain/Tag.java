package sori.jakku.kkunkkyu.memore.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Tag {

    @Id @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Tag(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public Tag(User user, Memo memo, String name) {
        this.user = user;
        this.name = name;
        this.memo = memo;
    }

}
