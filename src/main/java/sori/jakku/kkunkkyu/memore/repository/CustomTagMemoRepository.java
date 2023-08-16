package sori.jakku.kkunkkyu.memore.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.domain.*;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoListDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoUpdateDto;
import sori.jakku.kkunkkyu.memore.domain.dto.MemoWriteDto;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static sori.jakku.kkunkkyu.memore.domain.QMemo.*;
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
        em.persist(new Tag(user, tagDto.getTagA()));
        em.persist(new Tag(user, tagDto.getTagB()));
        em.persist(new Tag(user, tagDto.getTagC()));
        // 끝
        return tagDto;
    }

    @Transactional
    public void saveTagAndMemo(User user, MemoWriteDto memoWriteDto) {

        /**
         * 태그 있는지 확인 후, 있는데 DB 에 없으면 추가
         * 메모 추가
         * 태그메모 추가
         */
        em.merge(user);

        List<Tag> tagList = new ArrayList<>();
        if (memoWriteDto.getTag() != null) {
            memoWriteDto.getTag().forEach(
                    name -> {
                        Tag newTag = new Tag(user, name);
                        em.persist(newTag);
                        tagList.add(newTag);
                    }
            );
        }

        Memo memo = new Memo(user, memoWriteDto.getKeyword());

        if (memoWriteDto.getContent() != null) {
            memo.writeOnlyContent(memoWriteDto.getContent());
        }

        em.persist(memo);

        // 태그메모 추가
        tagList.forEach(tag ->
                em.persist(new TagMemo(memo, tag)));

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
                    // 태그가 이미 있는 거면 놔두고, 없으면 태그메모테이블과 태그에 새로 추가
                    if (value == true) {
                        Tag tag = tagRepository.findByNameAndUser(key, memo.getUser()).orElse(null);
                        if (tag == null) {
                            tag = tagRepository.save(new Tag(memo.getUser(), key));
                        }
                        em.persist(new TagMemo(findMemo, tag));
                    }
                    // 없애는 태그
                    if (value == false) {
                        Tag findTag = tagRepository.findByNameAndUser(key, memo.getUser()).orElse(null);
                        if (findTag == null) {
                            log.info("이미 없는 태그입니다.");
                            return;
                        }

                        TagMemo findTagMemo = query.select(tagMemo)
                                .where(tagMemo.memo.eq(findMemo), tagMemo.tag.eq(findTag))
                                .fetchFirst();

                        em.remove(findTagMemo);

                    }
            });

    }

    public void deleteMemo(Memo findMemo) {
        // 태그메모, 메모 삭제
        findMemo = em.merge(findMemo);

        List<TagMemo> tagMemoList = query.select(tagMemo)
                .where(memo.eq(findMemo))
                .fetchJoin()
                .fetch();

        tagMemoList.forEach(tm ->
                em.remove(tm));
        em.remove(findMemo);
    }

    public List<MemoListDto> findAllForList(Long id, Pageable pageable, String name) {

        List<Memo> memoList = query.select(memo)
                .from(memo, tag)
                .where(tag.name.eq(name), tag.memo.user.id.eq(id))
                .orderBy(tag.memo.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<MemoListDto> list = new ArrayList<>();

        if (name != null) {
            List<Tag> tagList = query.select(tag)
                    .from(tag)
                    .where(tag.memo.in(memoList))
                    .orderBy(tag.memo.id.desc())
                    .fetch();
            List<String> tags = new ArrayList<>();
            for (Memo memo : memoList) {
                for (Tag tag : tagList) {
                    if (memo == tag.getMemo()) {
                        tags.add(tag.getName());
                    }
                }
                list.add(new MemoListDto(memo.getKeyword(), memo.getContent(), tags));
            }
        }
        if (name == null) {
            for (Memo memo : memoList) {
                list.add(new MemoListDto(memo.getKeyword(), memo.getContent()));
            }
        }

        return list;

    }

    public void deleteTag(Long id, String name) throws MemoNotFoundException, UserNotFoundException {
        User user = em.find(User.class, id);
        if (user == null) {
            throw new UserNotFoundException();
        }

        Tag findTag = tagRepository.findByNameAndUser(name, user).orElse(null);

        if (findTag == null) {
            throw new MemoNotFoundException();
        }

        em.remove(findTag);
    }

    public List<String> findAllTag(Long id) {
        return query.select(tag.name)
                .from(tagMemo)
                .where(tagMemo.tag.user.id.eq(id))
                .fetchJoin()
                .fetch();
    }
}
