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
import sori.jakku.kkunkkyu.memore.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class TagControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clean() {
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("태그 쓰기 성공")
    void writeTagSuccess() throws Exception {
        // given
        String name = "태그 이름";

        User beUser = new User("user", "pwd");
        User user = userRepository.save(beUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", user.getId());


        // expected
        mockMvc.perform(
                post("/tag")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(name))
        ).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response").value("OK"))
                .andDo(print());
    }

    // 얘 일단 보류
//    @Test
//    @DisplayName("세션 id 없어서 실패")
//    void 세션_아이디_없음_실패() throws Exception {
//        String name = "태그이름";
//
//        MockHttpSession session = new MockHttpSession();
//
//        mockMvc.perform(
//                        post("/tag")
//                                .session(session)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(mapper.writeValueAsString(name))
//                ).andExpect(status().is5xxServerError())
//                .andDo(print());
//    }

    @Test
    @DisplayName("유저 없어서 실패")
    void 유저_없음_실패() throws Exception {
        String name = "태그";

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", 1L);

        mockMvc.perform(
                        post("/tag")
                                .session(session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(name))
                ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.response").value("USER_NOT_FOUND"))
                .andDo(print());
    }

    @Test
    @DisplayName("태그 길이 제한 실패")
    void 태그길이제한_실패() throws Exception {
        User findUser = new User("user", "pwd");
        User user = userRepository.save(findUser);
        String name = "doashdiosajdioajsodjaslkd";

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", user.getId());

        mockMvc.perform(
                        post("/tag")
                                .content(mapper.writeValueAsString(name))
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(session)
                ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.response").value("NOT_VALID"))
                .andDo(print());

    }

    @Test
    @DisplayName("태그 내용 없음 실패")
    void 태그내용없음_실패() throws Exception {
        User findUser = new User("user", "pwd");
        User user = userRepository.save(findUser);
        String name = " ";

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", user.getId());

        mockMvc.perform(
                        post("/tag")
                                .content(mapper.writeValueAsString(name))
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(session)
                ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.response").value("NOT_VALID"))
                .andDo(print());
    }

    @Test
    @DisplayName("태그 NULL 실패")
    void 태그NULL_실패() throws Exception {
        User findUser = new User("user", "pwd");
        User user = userRepository.save(findUser);
        String name = null;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", user.getId());

        mockMvc.perform(
                        post("/tag")
                                .content(mapper.writeValueAsString(name))
                                .contentType(MediaType.APPLICATION_JSON)
                                .session(session)
                ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.response").value("NOT_VALID"))
                .andDo(print());
    }

}