package sori.jakku.kkunkkyu.memore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.domain.Memo;
import sori.jakku.kkunkkyu.memore.domain.Tag;
import sori.jakku.kkunkkyu.memore.domain.TagMemo;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.repository.CustomTagMemoRepository;
import sori.jakku.kkunkkyu.memore.repository.MemoRepository;
import sori.jakku.kkunkkyu.memore.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemoServiceImplTest {

    @Autowired
    private UserRepository userRepository;
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
    @DisplayName("메모 쓰기 성공 : 모든 값")
    void 모든값쓰기_성공() throws DuplicateMemoException {
        User user = new User();
        User newUser = userRepository.save(user);

        List<String> list = new ArrayList<>();
        list.add("tag1");

        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);

        memoService.write(newUser.getId(), memoWriteDto);

        Memo memo = memoRepository.findByKeyword(memoWriteDto.getKeyword()).orElse(null);

        Tag tag = tagRepository.findByName(list.get(0)).orElse(null);

        List<TagMemo> tagMemoList = tagMemoRepository.findAllTagMemoByMemo(memo);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo(memoWriteDto.getKeyword());
        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo(list.get(0));
        assertThat(tagMemoList.size()).isNotEqualTo(0);

    }
    @Test
    @DisplayName("메모 쓰기 성공 : 키워드")
    void 키워드쓰기_성공() throws DuplicateMemoException {
        User user = new User();
        User newUser = userRepository.save(user);
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword");

        memoService.write(newUser.getId(), memoWriteDto);

        // 성공
        Memo memo = memoRepository.findByKeyword(memoWriteDto.getKeyword()).orElse(null);

        // 둘 다 없는 값
        Tag tag = tagRepository.findByName("noTag").orElse(null);
        List<TagMemo> tagMemoList = tagMemoRepository.findAllTagMemoByMemo(memo);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo(memoWriteDto.getKeyword());

        assertThat(tag).isNull();

        assertThat(tagMemoList.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("메모 쓰기 성공 : 키워드, 내용")
    void 키워드_내용_쓰기_성공() throws DuplicateMemoException {
        User user = new User();
        User newUser = userRepository.save(user);
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content");

        memoService.write(newUser.getId(), memoWriteDto);

        // 성공
        Memo memo = memoRepository.findByKeyword(memoWriteDto.getKeyword()).orElse(null);

        // 둘 다 없는 값
        Tag tag = tagRepository.findByName("noTag").orElse(null);
        List<TagMemo> tagMemoList = tagMemoRepository.findAllTagMemoByMemo(memo);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo(memoWriteDto.getKeyword());
        assertThat(memo.getContent()).isEqualTo(memoWriteDto.getContent());

        assertThat(tag).isNull();

        assertThat(tagMemoList.size()).isEqualTo(0);

    }
    @Test
    @DisplayName("메모 쓰기 성공 : 키워드, 태그")
    void 키워드_태그_쓰기_성공() throws DuplicateMemoException {
        User user = new User();
        User newUser = userRepository.save(user);

        List<String> list = new ArrayList<>();
        list.add("tag1");

        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", list);

        memoService.write(newUser.getId(), memoWriteDto);

        Memo memo = memoRepository.findByKeyword(memoWriteDto.getKeyword()).orElse(null);

        Tag tag = tagRepository.findByName(list.get(0)).orElse(null);

        List<TagMemo> tagMemoList = tagMemoRepository.findAllTagMemoByMemo(memo);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo(memoWriteDto.getKeyword());
        assertThat(memo.getContent()).isNull();

        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo(list.get(0));

        assertThat(tagMemoList.size()).isNotEqualTo(0);

    }

    @Test
    @DisplayName("메모 쓰기 실패 : 중복 키워드")
    void 메모쓰기_키워드중복_실패() throws DuplicateMemoException {
        User user = new User();
        User newUser = userRepository.save(user);

        List<String> list = new ArrayList<>();
        list.add("tag1");

        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);
        memoService.write(newUser.getId(), memoWriteDto);

        org.junit.jupiter.api.Assertions.
                assertThrows(DuplicateMemoException.class,
                () -> memoService.write(newUser.getId(), memoWriteDto));

    }


//    @Test
//    void changeKeyword() {
//    }
//
//    @Test
//    void changeContentTag() {
//    }
//
//    @Test
//    void removeMemo() {
//    }
}