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
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.config.jwt.TokenService;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.domain.dto.Response;
import sori.jakku.kkunkkyu.memore.repository.CustomTagMemoRepository;
import sori.jakku.kkunkkyu.memore.repository.MemoRepository;
import sori.jakku.kkunkkyu.memore.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemoControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebService webService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MemoService memoService;
    @Autowired
    private MemoRepository memoRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CustomTagMemoRepository tagMemoRepository;


    @BeforeEach
    void clean() {
        tagMemoRepository.deleteAllTagMemo();
        tagRepository.deleteAll();
        memoRepository.deleteAll();

    }

    @Test
    @DisplayName("태그 삭제 성공")
    void deleteMemo() throws Exception {
        User user = new User("username", "pwd1234");
        User newUser = userRepository.save(user);
        String token = tokenService.creatToken(newUser.getId(), newUser.getUsername());

        memoService.write(user.getId(), new MemoWriteDto("keyword", "content"));

        Map<String, String> map = new HashMap<>();
        map.put("keyword", "keyword");
        String keyword = webService.objectToJson(map);

        mockMvc.perform(
                        delete("/memo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                                .content(keyword))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response").value(Response.OK))
                .andDo(print());
    }

    @Test
    @DisplayName("메모 수정 성공")
    void changeMemo() throws Exception {
        User user = new User("username", "pwd1234");
        User newUser = userRepository.save(user);
        String token = tokenService.creatToken(newUser.getId(), newUser.getUsername());

        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);
        memoService.write(user.getId(), memoWriteDto);

        Map<String, Boolean> map = new HashMap<>();
        map.put("tag1", true);
        map.put("tag2", true);
        MemoUpdateDto memoUpdateDto = new MemoUpdateDto("keyword", "newKeyword", "content", map);

        mockMvc.perform(
                        put("/memo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                                .content(mapper.writeValueAsString(memoUpdateDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response").value(Response.OK))
                .andDo(print());

    }
    @Test
    @DisplayName("메모 수정 실패 : 본인 X")
    void changeMemo_본인아님_실패() throws Exception {
        User user = new User("username", "pwd1234");
        User userB = new User("usernameB", "pwd1234");
        User newUser = userRepository.save(user);
        User newUserB = userRepository.save(userB);
        String tokenB = tokenService.creatToken(newUserB.getId(), newUserB.getUsername());

        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);
        memoService.write(user.getId(), memoWriteDto);

        Map<String, Boolean> map = new HashMap<>();
        map.put("tag1", true);
        map.put("tag2", true);
        MemoUpdateDto memoUpdateDto = new MemoUpdateDto("keyword", "newKeyword", "content", map);

        mockMvc.perform(
                        put("/memo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenB)
                                .content(mapper.writeValueAsString(memoUpdateDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.response").value(Response.BAD))
                .andDo(print());

    }

    @Test
    @DisplayName("메모 쓰기 성공 : 모든 값")
    void write_all() throws Exception {
        User user = new User("username", "pwd1234");
        User newUser = userRepository.save(user);
        String token = tokenService.creatToken(newUser.getId(), newUser.getUsername());

        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);

        mockMvc.perform(
                        post("/memo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                                .content(mapper.writeValueAsString(memoWriteDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response").value(Response.OK))
                .andDo(print());
    }
    @Test
    @DisplayName("메모 쓰기 성공 : 이름만")
    void write_only_name() throws Exception {
        User user = new User("username", "pwd1234");
        User newUser = userRepository.save(user);
        String token = tokenService.creatToken(newUser.getId(), newUser.getUsername());

        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword");

        mockMvc.perform(
                        post("/memo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                                .content(mapper.writeValueAsString(memoWriteDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response").value(Response.OK))
                .andDo(print());
    }

    @Test
    @DisplayName("메모 쓰기 실패 : 중복 메모")
    void 메모실패_중복메모() throws Exception {
        User user = new User("username", "pwd1234");
        User newUser = userRepository.save(user);
        String token = tokenService.creatToken(newUser.getId(), newUser.getUsername());

        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword");

        memoService.write(user.getId(), memoWriteDto);

        mockMvc.perform(
                        post("/memo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                                .content(mapper.writeValueAsString(memoWriteDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.response").value(Response.DUPLICATE))
                .andDo(print());
    }
}