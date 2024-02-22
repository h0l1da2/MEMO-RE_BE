package sori.jakku.kkunkkyu.memore.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sori.jakku.kkunkkyu.memore.user.domain.User;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        this.user = mock(User.class);
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .build();
    }

    @Test
    @DisplayName("인코딩 된 비밀번호를 유저에 넣는 작업")
    void encodedPassword() {
        String encodedPassword = "encoded password";

        User user = createUser();
        User sameUser = createUser();

        user.encodedPassword(encodedPassword);

        assertNotEquals(user.getPassword(), sameUser.getPassword());
    }
}
