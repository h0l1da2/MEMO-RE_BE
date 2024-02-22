package sori.jakku.kkunkkyu.memore.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sori.jakku.kkunkkyu.memore.memo.domain.Memo;
import sori.jakku.kkunkkyu.memore.user.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

public class MemoTest {

    private Memo memo;

    @BeforeEach
    void setUp() {
        this.memo = mock(Memo.class);
    }

    private User createUser(Long userId) {
        return User.builder()
                .id(userId)
                .username("username")
                .password("password")
                .build();
    }

    private Memo createMemo() {
        return Memo.builder()
                .id(1L)
                .keyword("초기 키워드")
                .content("초기 내용")
                .build();
    }

    @Test
    @DisplayName("메모 제목, 내용 변경")
    void updateMemoKeywordAndContent() {
        String keyword = "키워드 변경";
        String content = "내용 변경";

        Memo memo = createMemo();
        Memo sameMemo = createMemo();

        memo.changeMemo(keyword, content);

        assertNotEquals(memo.getKeyword(), sameMemo.getKeyword());
        assertNotEquals(memo.getContent(), sameMemo.getContent());

        assertEquals(memo.getKeyword(), keyword);
        assertEquals(memo.getContent(), content);
    }

    @Test
    @DisplayName("메모 내용만 변경")
    void updateMemoContent() {
        String content = "내용 변경";

        Memo memo = createMemo();
        Memo sameMemo = createMemo();

        memo.writeOnlyContent(content);

        assertEquals(memo.getKeyword(), sameMemo.getKeyword());

        assertNotEquals(memo.getContent(), sameMemo.getContent());
    }
}
