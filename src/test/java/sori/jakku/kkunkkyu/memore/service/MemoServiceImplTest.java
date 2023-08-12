package sori.jakku.kkunkkyu.memore.service;

import org.junit.jupiter.api.Assertions;
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
import sori.jakku.kkunkkyu.memore.domain.dto.ConTagUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.repository.CustomTagMemoRepository;
import sori.jakku.kkunkkyu.memore.repository.MemoRepository;
import sori.jakku.kkunkkyu.memore.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.service.inter.MemoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    @DisplayName("메모 수정 성공 : 모든 값")
    void 메모수정_성공() throws DuplicateMemoException, UserNotFoundException, MemoNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);
        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);
        memoService.write(newUser.getId(), memoWriteDto);

        ConTagUpdateDto conTagUpdateDto = new ConTagUpdateDto();
        conTagUpdateDto.setOriginKey("keyword");
        conTagUpdateDto.setNewKey("newKey");
        conTagUpdateDto.setContent("newCont");
        Map<String, Boolean> map = new HashMap();
        map.put("tag2", true);
        map.put("tag1", false);
        conTagUpdateDto.setTag(map);

        memoService.changeContentTag(newUser.getId(), conTagUpdateDto);

        Memo memo = memoRepository.findByKeyword("newKey").orElse(null);
        Tag tag1 = tagRepository.findByName("tag1").orElse(null);
        Tag tag2 = tagRepository.findByName("tag2").orElse(null);
        List<TagMemo> allTagMemoByMemo = tagMemoRepository.findAllTagMemoByMemo(memo);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo("newKey");
        assertThat(memo.getContent()).isEqualTo("newCont");
        assertThat(tag1).isNotNull();
        assertThat(tag2).isNotNull();
        assertThat(allTagMemoByMemo.get(0).getTag().getId()).isEqualTo(tag2.getId());
        assertThat(allTagMemoByMemo.size()).isEqualTo(1);

    }
    @Test
    @DisplayName("메모 수정 성공 : 태그 없음")
    void 메모수정_태그없음_성공() throws DuplicateMemoException, UserNotFoundException, MemoNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);
        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);
        memoService.write(newUser.getId(), memoWriteDto);

        ConTagUpdateDto conTagUpdateDto = new ConTagUpdateDto();
        conTagUpdateDto.setOriginKey("keyword");
        conTagUpdateDto.setNewKey("newKey");
        conTagUpdateDto.setContent("newCont");
        Map<String, Boolean> map = new HashMap();
        map.put("tag1", false);
        conTagUpdateDto.setTag(map);

        memoService.changeContentTag(newUser.getId(), conTagUpdateDto);

        Memo memo = memoRepository.findByKeyword("newKey").orElse(null);
        Tag tag1 = tagRepository.findByName("tag1").orElse(null);
        List<TagMemo> allTagMemoByMemo = tagMemoRepository.findAllTagMemoByMemo(memo);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo("newKey");
        assertThat(memo.getContent()).isEqualTo("newCont");
        assertThat(tag1).isNotNull();
        assertThat(allTagMemoByMemo.size()).isEqualTo(0);


    }
    @Test
    @DisplayName("메모 수정 실패 : 키워드 실패")
    void 메모수정_키워드_실패() throws DuplicateMemoException {
        User user = new User();
        User newUser = userRepository.save(user);
        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);
        memoService.write(newUser.getId(), memoWriteDto);

        ConTagUpdateDto conTagUpdateDto = new ConTagUpdateDto();
        conTagUpdateDto.setOriginKey("no_keyword");
        conTagUpdateDto.setNewKey("newKey");
        conTagUpdateDto.setContent("newCont");
        Map<String, Boolean> map = new HashMap();
        map.put("tag2", true);
        map.put("tag1", false);
        conTagUpdateDto.setTag(map);

        Assertions.assertThrows(MemoNotFoundException.class, () ->
                memoService.changeContentTag(newUser.getId(), conTagUpdateDto));
    }

    @Test
    @DisplayName("메모 삭제 성공")
    void removeMemo() throws DuplicateMemoException, UserNotFoundException, MemoNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);
        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);
        memoService.write(newUser.getId(), memoWriteDto);

        memoService.removeMemo(user.getId(), "keyword");

        Memo memo = memoRepository.findByKeyword("keyword").orElse(null);
        Tag tag = tagRepository.findByName("tag1").orElse(null);
        List<TagMemo> allTagMemoByMemo = tagMemoRepository.findAllTagMemo();

        assertThat(memo).isNull();
        assertThat(tag).isNotNull();
        assertThat(allTagMemoByMemo.size()).isEqualTo(0);
    }
}