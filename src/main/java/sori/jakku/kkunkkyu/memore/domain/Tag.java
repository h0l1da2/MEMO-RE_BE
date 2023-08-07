package sori.jakku.kkunkkyu.memore.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Tag {

    @Id @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
