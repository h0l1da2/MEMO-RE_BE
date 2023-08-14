package sori.jakku.kkunkkyu.memore.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Memo(User user) {
        this.user = user;
    }
    public void changeMemo(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }

    public void writeOnlyKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void writeOnlyContent(String content) {
        this.content = content;
    }
}
