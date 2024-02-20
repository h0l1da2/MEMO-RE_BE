package sori.jakku.kkunkkyu.memore.memo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.user.domain.User;

@Getter
@Entity
@NoArgsConstructor
public class Memo {

    @Id @GeneratedValue
    private Long id;
    private String keyword;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Memo(User user, String keyword) {
        this.user = user;
        this.keyword = keyword;
    }
    public void changeMemo(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }

    public void writeOnlyContent(String content) {
        this.content = content;
    }
}
