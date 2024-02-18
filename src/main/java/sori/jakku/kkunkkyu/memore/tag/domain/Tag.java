package sori.jakku.kkunkkyu.memore.tag.domain;

import jakarta.persistence.*;
import lombok.*;
import sori.jakku.kkunkkyu.memore.user.domain.User;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tag {

    @Id @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
