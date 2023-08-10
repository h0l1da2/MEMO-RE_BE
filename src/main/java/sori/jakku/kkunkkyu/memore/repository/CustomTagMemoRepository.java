package sori.jakku.kkunkkyu.memore.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.domain.*;
import sori.jakku.kkunkkyu.memore.domain.dto.ConTagUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;

import java.util.ArrayList;
import java.util.List;

import static sori.jakku.kkunkkyu.memore.domain.QTag.*;
import static sori.jakku.kkunkkyu.memore.domain.QTagMemo.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomTagMemoRepository {

    private final TagRepository tagRepository;
    private final EntityManager em;
    private final JPAQueryFactory query;

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

    @Transactional
    public void updateMemoAndTag(Memo memo, ConTagUpdateDto conTagUpdateDto) {
        /**
         * 컨텐츠바꾸고
         * 태그메모테이블에서 해당 태그가 달린 레코드 삭제
         */

        Memo findMemo = em.merge(memo);
        findMemo.changeContent(conTagUpdateDto.getContent());

        conTagUpdateDto.getTag().forEach((key, value) -> {
                    if (value == false) {
                        Tag removeTag = query.select(tag)
                                .from(tag)
                                .where(tag.name.eq(key))
                                .fetchFirst();

                        query.delete(tagMemo)
                                .where(tagMemo.memo.eq(findMemo).and(
                                        tagMemo.tag.eq(removeTag)
                                ));

                    }
                }
                );

    }

    public void deleteMemo(Memo memo) {
        memo = em.merge(memo);

        query.delete(tagMemo)
                .where(tagMemo.memo.eq(memo));

        em.remove(memo);
    }
}
