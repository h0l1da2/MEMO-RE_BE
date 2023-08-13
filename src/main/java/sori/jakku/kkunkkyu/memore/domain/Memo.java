package sori.jakku.kkunkkyu.memore.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;

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

    public Memo(MemoWriteDto memoWriteDto, User user) {
        this.keyword = memoWriteDto.getKeyword();
        this.content = memoWriteDto.getContent();
        this.user = user;
    }

    public void changeMemo(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }
}
