package sori.jakku.kkunkkyu.memore.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sori.jakku.kkunkkyu.memore.tag.domain.Tag;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.tag.dto.TagDto;
import sori.jakku.kkunkkyu.memore.common.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.common.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.common.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.tag.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.user.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.tag.service.TagUseCase;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TagServiceImplTest {

    @Autowired
    private TagUseCase tagService;
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
    @DisplayName("태그 쓰기 성공")
    void writeSuccess() throws UserNotFoundException, ConditionNotMatchException, DuplicateMemoException {
        // given 유저, name
        User user = new User("user", "pwd");
        user = userRepository.save(user);
        String name = "태그";
        // when
        name = tagService.writeTag(user.getId(), name);

        // then
        Tag tag = tagRepository.findByNameAndUser(name, user).orElse(null);
        assertThat(name).isNotNull();
        assertThat(name).isEqualTo("태그");
        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo(name);
    }
    @Test
    @DisplayName("태그 쓰기 실패 : 유저 로그인 X")
    void writeLoginFail() throws ConditionNotMatchException {
        // given 유저, name
        String name = "태그";
        // when then
        Assertions.assertThrows(
                UserNotFoundException.class, () ->
                tagService.writeTag(1L, name));
    }
    @Test
    @DisplayName("태그 쓰기 실패 : 태그 NULL X")
    void writeNullFail() throws UserNotFoundException {
        // given 유저, name
        User user = new User("user", "pwd");
        user = userRepository.save(user);
        String name = null;
        User finalUser = user;
        // when then
        Assertions.assertThrows(
                ConditionNotMatchException.class, () ->
                tagService.writeTag(finalUser.getId(), name));
    }
    @Test
    @DisplayName("태그 쓰기 실패 : 태그 내용 없음")
    void writeNotConFail() throws UserNotFoundException {
        // given 유저, name
        User user = new User("user", "pwd");
        user = userRepository.save(user);
        String name = "";
        User finalUser = user;
        // when then
        Assertions.assertThrows(
                ConditionNotMatchException.class, () ->
                tagService.writeTag(finalUser.getId(), name));
    }
    @Test
    @DisplayName("태그 쓰기 실패 : 태그 길이 Over")
    void writeLengthOverFail() {
        // given 유저, name
        User user = new User("user", "pwd");
        user = userRepository.save(user);
        String name = "태그3213123432423423231";
        User finalUser = user;
        // when then
        Assertions.assertThrows(
                ConditionNotMatchException.class, () ->
                tagService.writeTag(finalUser.getId(), name));
    }
}