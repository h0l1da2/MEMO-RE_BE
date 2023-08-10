package sori.jakku.kkunkkyu.memore.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.domain.Memo;
import sori.jakku.kkunkkyu.memore.domain.Tag;
import sori.jakku.kkunkkyu.memore.domain.TagMemo;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomTagMemoRepository {

    private final TagRepository tagRepository;
    private final EntityManager em;

    @Transactional
    public TagDto saveTagMain(User user, TagDto tagDto) {
        // 트랜잭션 시작
        tagRepository.save(new Tag(user, tagDto.getTagA()));
        tagRepository.save(new Tag(user, tagDto.getTagB()));
        tagRepository.save(new Tag(user, tagDto.getTagC()));
        // 끝
        return tagDto;
    }

    @Transactional
    public void saveTagAndMemo(User user, MemoWriteDto memoWriteDto) {

        /**
         * 태그 없으면 추가, 불러오기
         * 태그 메모 연결 테이블에 + 메모 후 DB 추가
         */

        // 태그 추가, 불러오기
        List<Tag> list = new ArrayList<>();
        for (String name : memoWriteDto.getTag()) {
            list.add(tagRepository.findByName(name).orElse(
                    tagRepository.save(new Tag(user, name)))
            );
        }

        // 메모 추가 후, 태그 메모 테이블에 추가
        Memo memo = new Memo(memoWriteDto.getKeyword(), memoWriteDto.getContent(), user);
        em.persist(memo);
        for (Tag tag : list) {
            TagMemo tagMemo = new TagMemo(memo, tag);
            em.persist(tagMemo);
        }

        em.close();

    }
}
