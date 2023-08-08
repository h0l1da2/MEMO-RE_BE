package sori.jakku.kkunkkyu.memore.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;
import sori.jakku.kkunkkyu.memore.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.service.inter.TagService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TagServiceImplTest {

    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;
    private

    @BeforeEach
    void clean() {
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("성공: 유저 유무와 태그 길이 검사 후 DB 추가, Json 리턴")
    void writeForMainSuccess() throws UserNotFoundException, ConditionNotMatchException {
        // given 유저와 태그DTO
        User user = userRepository.save(new User("user", "pwd"));
        TagDto tagDto = new TagDto("tagA", "tagB", "tagC");

        // when
        String tagJson = tagService.writeForMain(user.getId(), tagDto);

        // then json이 나온다 굿?

        Gson gson = new Gson();
        String json = gson.toJson(tagDto);

        assertThat(tagJson).isNotNull();
        assertThat(tagJson).isEqualTo(json);

    }

    @Test
    @DisplayName("메인메모쓰기:유저없어서 실패")
    void writeForMainUserNotFoundFail() throws ConditionNotMatchException {
        // given
        TagDto tagDto = new TagDto("tagA", "tagB", "tagC");

        // when then
        org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class, () ->
                        tagService.writeForMain(1L, tagDto));

    }

    @Test
    @DisplayName("메인메모쓰기:태그길이 길어서 실패")
    void writeForMain() throws UserNotFoundException {
        // given 유저와 태그DTO
        User user = userRepository.save(new User("user", "pwd"));
        TagDto tagDto = new TagDto("tagA111111111111111111111", "tagB", "tagC");

        // when then
        org.junit.jupiter.api.Assertions.assertThrows(
                ConditionNotMatchException.class, () ->
                tagService.writeForMain(user.getId(), tagDto));

    }
}