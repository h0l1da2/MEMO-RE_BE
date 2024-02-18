package sori.jakku.kkunkkyu.memore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sori.jakku.kkunkkyu.memore.common.config.jwt.TokenUseCase;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.tag.dto.MainTagSaveDto;
import sori.jakku.kkunkkyu.memore.tag.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.user.repository.UserRepository;

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
    @Autowired
    private TokenUseCase tokenService;

    @BeforeEach
    void clean() {
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("메인 태그 추가 성공")
    void tagMainSuccess() throws Exception {
        // given : tagDTO, User, Session
        MainTagSaveDto mainTagSaveDto = new MainTagSaveDto("tagA", "tagB", "tagC");
        User user = new User("user", "pwd");
        user = userRepository.save(user);

        String token = tokenService.creatToken(user.getId(), user.getUsername());

        // expected
        mockMvc.perform(
                        post("/")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(mainTagSaveDto))
                ).andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
    @Test
    @DisplayName("메인 태그 길이 20자 이상 실패")
    void tagMainLengthFail() throws Exception {
        // given : tagDTO, User, Session
        MainTagSaveDto mainTagSaveDto = new MainTagSaveDto("tagA12344ㅈ도ㅕ3294842ㅠㅓㅜ32356789", "tagB", "tagC");
        User user = new User("user", "pwd");
        user = userRepository.save(user);

        String token = tokenService.creatToken(user.getId(), user.getUsername());

        // expected
        mockMvc.perform(
                        post("/")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(mainTagSaveDto))
                ).andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("메인 태그 없는 유저 요청 실패")
    void tagMainUserNotFoundFail() throws Exception {
        // given : tagDTO, User, Session
        MainTagSaveDto mainTagSaveDto = new MainTagSaveDto();

        User user = new User("user", "pwd");
        user = userRepository.save(user);
        String token = tokenService.creatToken(user.getId(), user.getUsername());


        // expected
        mockMvc.perform(
                        post("/")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(mainTagSaveDto))
                ).andExpect(status().is4xxClientError())
                .andDo(print());
    }

}