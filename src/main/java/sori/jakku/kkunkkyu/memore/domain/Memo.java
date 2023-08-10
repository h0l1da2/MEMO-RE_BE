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

    public Memo(String keyword, String content, User user) {
        this.keyword = keyword;
        this.content = content;
        this.user = user;
    }

    public void changeKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
