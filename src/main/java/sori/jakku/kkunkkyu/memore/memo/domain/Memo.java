package sori.jakku.kkunkkyu.memore.memo.domain;

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
public class Memo {

    @Id @GeneratedValue
    private Long id;
    private String keyword;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changeMemo(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }

    public void writeOnlyContent(String content) {
        this.content = content;
    }

    // 보안을 위해 메모가 없다는 메시지를 띄운다.
    public void checkAuthorizedUser(Long requesterId) {
        if(!user.getId().equals(requesterId)){
            throw new BadRequestException(Exception.MEMO_NOT_FOUND);
        }
    }
}
