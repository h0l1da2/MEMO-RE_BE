package sori.jakku.kkunkkyu.memore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.memo.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.common.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.common.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.common.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.memo.domain.Memo;
import sori.jakku.kkunkkyu.memore.tagmemo.repository.CustomTagMemoRepository;
import sori.jakku.kkunkkyu.memore.memo.repository.MemoRepository;
import sori.jakku.kkunkkyu.memore.tag.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.user.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.memo.service.MemoUseCase;
import sori.jakku.kkunkkyu.memore.tag.domain.Tag;
import sori.jakku.kkunkkyu.memore.user.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemoUseCaseImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemoUseCase memoService;
    @Autowired
    private MemoRepository memoRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CustomTagMemoRepository tagMemoRepository;

    @BeforeEach
    void clean() {
        tagRepository.deleteAll();
        memoRepository.deleteAll();
        tagMemoRepository.deleteAll();

    }

    @Test
    @DisplayName("메모 쓰기 성공 : 모든 값")
    void 모든값쓰기_성공() throws DuplicateMemoException, UserNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);

        List<String> list = new ArrayList<>();
        list.add("tag1");

        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);

        memoService.write(newUser.getId(), memoWriteDto);

        Memo memo = memoRepository.findByKeywordAndUser(memoWriteDto.getKeyword(), newUser).orElse(null);

        Tag tag = tagRepository.findByNameAndUser(list.get(0), newUser).orElse(null);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo(memoWriteDto.getKeyword());
        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo(list.get(0));

    }
    @Test
    @DisplayName("메모 쓰기 성공 : 키워드")
    void 키워드쓰기_성공() throws DuplicateMemoException, UserNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword");

        memoService.write(newUser.getId(), memoWriteDto);

        // 성공
        Memo memo = memoRepository.findByKeywordAndUser(memoWriteDto.getKeyword(), newUser).orElse(null);

        // 둘 다 없는 값
        Tag tag = tagRepository.findByNameAndUser("noTag", newUser).orElse(null);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo(memoWriteDto.getKeyword());

        assertThat(tag).isNull();


    }

    @Test
    @DisplayName("메모 쓰기 성공 : 키워드, 내용")
    void 키워드_내용_쓰기_성공() throws DuplicateMemoException, UserNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content");

        memoService.write(newUser.getId(), memoWriteDto);

        // 성공
        Memo memo = memoRepository.findByKeywordAndUser(memoWriteDto.getKeyword(), newUser).orElse(null);

        // 둘 다 없는 값
        Tag tag = tagRepository.findByNameAndUser("noTag", newUser).orElse(null);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo(memoWriteDto.getKeyword());
        assertThat(memo.getContent()).isEqualTo(memoWriteDto.getContent());

        assertThat(tag).isNull();


    }
    @Test
    @DisplayName("메모 쓰기 성공 : 키워드, 태그")
    void 키워드_태그_쓰기_성공() throws DuplicateMemoException, UserNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);

        List<String> list = new ArrayList<>();
        list.add("tag1");

        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", list);

        memoService.write(newUser.getId(), memoWriteDto);

        Memo memo = memoRepository.findByKeywordAndUser(memoWriteDto.getKeyword(), newUser).orElse(null);

        Tag tag = tagRepository.findByNameAndUser(list.get(0), newUser).orElse(null);


        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo(memoWriteDto.getKeyword());
        assertThat(memo.getContent()).isNull();

        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo(list.get(0));


    }

    @Test
    @DisplayName("메모 쓰기 실패 : 중복 키워드")
    void 메모쓰기_키워드중복_실패() throws DuplicateMemoException, UserNotFoundException {
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

        MemoUpdateDto memoUpdateDto = new MemoUpdateDto();
        memoUpdateDto.setOriginKey("keyword");
        memoUpdateDto.setNewKey("newKey");
        memoUpdateDto.setContent("newCont");
        Map<String, Boolean> map = new HashMap();
        map.put("tag2", true);
        map.put("tag1", false);
        memoUpdateDto.setTag(map);

        memoService.changeMemo(newUser.getId(), memoUpdateDto);

        Memo memo = memoRepository.findByKeywordAndUser("newKey", newUser).orElse(null);
        Tag tag1 = tagRepository.findByNameAndUser("tag1", newUser).orElse(null);
        Tag tag2 = tagRepository.findByNameAndUser("tag2", newUser).orElse(null);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo("newKey");
        assertThat(memo.getContent()).isEqualTo("newCont");
        assertThat(tag1).isNotNull();
        assertThat(tag2).isNotNull();

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

        MemoUpdateDto memoUpdateDto = new MemoUpdateDto();
        memoUpdateDto.setOriginKey("keyword");
        memoUpdateDto.setNewKey("newKey");
        memoUpdateDto.setContent("newCont");
        Map<String, Boolean> map = new HashMap();
        map.put("tag1", false);
        memoUpdateDto.setTag(map);

        memoService.changeMemo(newUser.getId(), memoUpdateDto);

        Memo memo = memoRepository.findByKeywordAndUser("newKey", newUser).orElse(null);
        Tag tag1 = tagRepository.findByNameAndUser("tag1", newUser).orElse(null);

        assertThat(memo).isNotNull();
        assertThat(memo.getKeyword()).isEqualTo("newKey");
        assertThat(memo.getContent()).isEqualTo("newCont");
        assertThat(tag1).isNotNull();


    }
    @Test
    @DisplayName("메모 수정 실패 : 키워드 실패")
    void 메모수정_키워드_실패() throws DuplicateMemoException, UserNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);
        List<String> list = new ArrayList<>();
        list.add("tag1");
        MemoWriteDto memoWriteDto = new MemoWriteDto("keyword", "content", list);
        memoService.write(newUser.getId(), memoWriteDto);

        MemoUpdateDto memoUpdateDto = new MemoUpdateDto();
        memoUpdateDto.setOriginKey("no_keyword");
        memoUpdateDto.setNewKey("newKey");
        memoUpdateDto.setContent("newCont");
        Map<String, Boolean> map = new HashMap();
        map.put("tag2", true);
        map.put("tag1", false);
        memoUpdateDto.setTag(map);

        Assertions.assertThrows(MemoNotFoundException.class, () ->
                memoService.changeMemo(newUser.getId(), memoUpdateDto));
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

        Memo memo = memoRepository.findByKeywordAndUser("keyword", newUser).orElse(null);
        Tag tag = tagRepository.findByNameAndUser("tag1", newUser).orElse(null);

        assertThat(memo).isNull();
        assertThat(tag).isNotNull();
    }
    @Test
    @DisplayName("메모 리스트 : 태그 없음")
    void memoList_null() throws DuplicateMemoException, UserNotFoundException {
        User user = new User();
        User newUser = userRepository.save(user);

        List<String> list = new ArrayList<>();
        list.add("tag1");

        for (int i = 0; i < 10; i++) {
            MemoWriteDto memoWriteDto = new MemoWriteDto("keyword"+i, "content", list);
            memoService.write(newUser.getId(), memoWriteDto);
        }

        List<MemoListDto> memoList = memoService.memoList(user.getId(), PageRequest.of(0, 12), null);
        assertThat(memoList.size()).isEqualTo(0);
        for (int i = 0; i < memoList.size(); i++) {
            System.out.println("keyword = " + memoList.get(i).getKeyword());
            System.out.println("content = " + memoList.get(i).getContent());
        }
    }

    @Test
    @DisplayName("메모 리스트 : 태그 한개")
    void memoList_one() throws Exception {
        User user = new User();
        User newUser = userRepository.save(user);

        List<String> list = new ArrayList<>();
        list.add("tag1");

        for (int i = 0; i < 10; i++) {
            MemoWriteDto memoWriteDto = new MemoWriteDto("keyword"+i, "content", list);
            memoService.write(newUser.getId(), memoWriteDto);
        }

        List<MemoListDto> memoList = memoService.memoList(user.getId(), PageRequest.of(0, 12), list.get(0));
        assertThat(memoList.size()).isEqualTo(10);
        for (int i = 0; i < memoList.size(); i++) {
            System.out.println("keyword = " + memoList.get(i).getKeyword());
            System.out.println("content = " + memoList.get(i).getContent());
            memoList.get(i).getTag().forEach(tag ->
                System.out.println("tag = " + tag)
            );
        }
    }
    @Test
    @DisplayName("메모 리스트 : 태그 두개")
    void memoList_Two() throws Exception {
        User user = new User();
        User newUser = userRepository.save(user);

        List<String> list = new ArrayList<>();
        list.add("tag1");
        list.add("tag2");

        for (int i = 0; i < 10; i++) {
            MemoWriteDto memoWriteDto = new MemoWriteDto("keyword"+i, "content", list);
            memoService.write(newUser.getId(), memoWriteDto);
        }

        List<MemoListDto> memoList = memoService.memoList(user.getId(), PageRequest.of(0, 12), "tag2");
        assertThat(memoList.size()).isEqualTo(10);
        for (int i = 0; i < memoList.size(); i++) {
            System.out.println("keyword = " + memoList.get(i).getKeyword());
            System.out.println("content = " + memoList.get(i).getContent());
            memoList.get(i).getTag().forEach(tag ->
                System.out.println("tag = " + tag)
            );
        }
    }
}