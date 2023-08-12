package sori.jakku.kkunkkyu.memore.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.domain.*;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoUpdateDto;
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
        Memo memo = new Memo(memoWriteDto.getKeyword(), memoWriteDto.getContent(), user);

        // 태그가 있을 경우, 태그 추가 및 불러오기
        if (memoWriteDto.getTag() != null) {
            List<Tag> list = new ArrayList<>();
            for (String name : memoWriteDto.getTag()) {
                Tag findTag = query
                        .select(tag)
                        .from(tag)
                        .where(tag.user.eq(user).and(tag.name.eq(name)))
                        .fetchFirst();

                if (findTag == null) {
                    findTag = new Tag(user, name);
                    em.persist(findTag);
                }
                list.add(findTag);

            }
            // 메모 추가 후, 태그 메모 테이블에 추가
            em.persist(memo);
            for (Tag tag : list) {
                TagMemo tagMemo = new TagMemo(memo, tag);
                em.persist(tagMemo);
            }
        } else {
            em.persist(memo);
        }

        em.close();

    }

    @Transactional
    public void updateMemoAndTag(Memo memo, MemoUpdateDto memoUpdateDto) {
        /**
         * 컨텐츠바꾸고
         * 태그메모테이블에서 해당 태그가 달린 레코드 삭제
         */

        Memo findMemo = em.merge(memo);
        findMemo.changeMemo(memoUpdateDto.getNewKey(), memoUpdateDto.getContent());

        memoUpdateDto.getTag().forEach((key, value) -> {
                    if (value == false) {

                        Tag removeTag = query.select(tag)
                                .from(tag)
                                .where(tag.name.eq(key).and(tag.user.eq(findMemo.getUser())))
                                .fetchFirst();

                        query.delete(tagMemo)
                                .where(tagMemo.memo.eq(findMemo).and(
                                        tagMemo.tag.eq(removeTag))).execute();

                    }
                    // 태그가 이미 있는 거면 놔두고, 없으면 태그메모테이블과 태그에 새로 추가
                    if (value == true) {
                        Tag findTag = tagRepository.findByName(key).orElse(null);
                        if (findTag == null) {
                            Tag newTag = tagRepository.save(new Tag(memo.getUser(), key));
                            em.persist(new TagMemo(memo, newTag));
                        }
                    }
                }
                );

    }

    public void deleteMemo(Memo memo) {
        memo = em.merge(memo);

        query.delete(tagMemo)
                .where(tagMemo.memo.eq(memo))
                .execute();

        em.remove(memo);
    }

    public void deleteAllTagMemo() {
        query.delete(tagMemo);
    }

    public List<TagMemo> findAllTagMemo() {
        return query.select(tagMemo)
                .from(tagMemo)
                .fetch();
    }

    public List<TagMemo> findAllTagMemoByMemo(Memo memo) {
        return query.select(tagMemo)
                .from(tagMemo)
                .where(tagMemo.memo.eq(memo))
                .fetch();

    }
}
