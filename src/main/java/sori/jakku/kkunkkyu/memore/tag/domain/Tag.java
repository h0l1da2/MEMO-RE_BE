package sori.jakku.kkunkkyu.memore.tag.domain;

import jakarta.persistence.*;
import lombok.*;
import sori.jakku.kkunkkyu.memore.common.exception.BadRequestException;
import sori.jakku.kkunkkyu.memore.common.exception.Exception;
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

    // 보안을 위해 태그가 없다는 메시지를 띄운다.
    public void checkAuthorizedUser(Long requesterId) {
        if(!user.getId().equals(requesterId)){
            throw new BadRequestException(Exception.TAG_NOT_FOUND);
        }
    }

}
