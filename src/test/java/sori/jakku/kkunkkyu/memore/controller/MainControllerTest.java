package sori.jakku.kkunkkyu.memore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;
import sori.jakku.kkunkkyu.memore.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void clean() {
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("메인 태그 추가 성공")
    void tagMainSuccess() throws Exception {
        // given : tagDTO, User, Session
        TagDto tagDto = new TagDto("tagA", "tagB", "tagC");
        User user = new User("user", "pwd");
        user = userRepository.save(user);

        // 세션 id 셋팅
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", user.getId());

        // expected
        mockMvc.perform(
                        post("/")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(tagDto))
                ).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response").value("OK"))
                .andDo(print());
    }
    @Test
    @DisplayName("메인 태그 길이 10자 이상 실패")
    void tagMainLengthFail() throws Exception {
        // given : tagDTO, User, Session
        TagDto tagDto = new TagDto("tagA123456789", "tagB", "tagC");
        User user = new User("user", "pwd");
        user = userRepository.save(user);

        // 세션 id 셋팅
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", user.getId());

        // expected
        mockMvc.perform(
                        post("/")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(tagDto))
                ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.response").value("TAG_NOT_VALID"))
                .andDo(print());
    }

    @Test
    @DisplayName("메인 태그 없는 유저 요청 실패")
    void tagMainUserNotFoundFail() throws Exception {
        // given : tagDTO, User, Session
        TagDto tagDto = new TagDto("tagA", "tagB", "tagC");

        // 세션 id 셋팅
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", 1L);

        // expected
        mockMvc.perform(
                        post("/")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(tagDto))
                ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.response").value("USER_NOT_FOUND"))
                .andDo(print());
    }

}