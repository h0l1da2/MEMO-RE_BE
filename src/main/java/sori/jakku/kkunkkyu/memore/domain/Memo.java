package sori.jakku.kkunkkyu.memore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
