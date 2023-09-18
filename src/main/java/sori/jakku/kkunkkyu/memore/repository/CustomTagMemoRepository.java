package sori.jakku.kkunkkyu.memore.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.domain.*;
import sori.jakku.kkunkkyu.memore.domain.dto.*;
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

        Memo memo = new Memo(user, memoWriteDto.getKeyword());

        String memoContent = memoWriteDto.getContent();

        if (memoContent != null) {
            memo.writeOnlyContent(memoWriteDto.getContent());
        }

        em.persist(memo);

        List<Tag> tagList = new ArrayList<>();

        List<String> dtoTagList = memoWriteDto.getTag();

        if (dtoTagList != null) {
            dtoTagList.stream().forEach(
                    name -> {
                        Tag newTag = new Tag(user, name);
                        em.persist(newTag);
                        tagList.add(newTag);
                    });
        }

        // 태그메모 추가
        tagList.stream().forEach(tag ->
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
                            tag = new Tag(memo.getUser(), key);
                            em.persist(tag);
                        }
                        em.persist(new TagMemo(findMemo, tag));
                    }
                    // 없애는 태그
                    if (value == false) {
                        Tag findTag = tagRepository.findByNameAndUser(key, memo.getUser()).orElse(null);

                        TagMemo findTagMemo = null;

                        if (findTag == null) {

                            findTagMemo = query
                                    .select(tagMemo)
                                    .from(tagMemo)
                                    .where(tagMemo.memo.eq(findMemo), tagMemo.tag.isNull())
                                    .fetchFirst();

                        }

                        if (findTag != null) {

                            findTagMemo = query
                                    .select(tagMemo)
                                    .from(tagMemo)
                                    .where(tagMemo.memo.eq(findMemo), tagMemo.tag.isNotNull())
                                    .fetchFirst();

                        }

                        if (findTagMemo != null) {
                            log.info("해당 태그 메모 삭제");
                            em.remove(findTagMemo);
                        }
                    }
            });

    }

    @Transactional
    public void deleteMemo(Memo findMemo) {
        // 태그메모, 메모 삭제
        findMemo = em.merge(findMemo);

        List<TagMemo> tagMemoList = query.select(tagMemo)
                .from(tagMemo)
                .where(tagMemo.memo.in(findMemo))
                .fetch();

            tagMemoList.stream().forEach(tm ->
                    em.remove(tm));

        em.remove(findMemo);
    }

    public List<MemoListDto> findAllForList(Long id, Pageable pageable, String name) {
        /**
         * 태그, 태그메모로 메모 가져오기
         * 그걸로 태그 가져오기 !
         */
        List<Tuple> tupleMemoTagList = new ArrayList<>();

        // 태그가 null 이 아니라면
        if (name != null) {

            tupleMemoTagList = query.select(memo.keyword, memo.content, tag.name)
                    .from(tagMemo)
                    .leftJoin(tagMemo.memo, memo)
                    .leftJoin(tagMemo.tag, tag)
                    .where(memo.user.id.eq(id), tag.name.eq(name))
                    .orderBy(memo.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            // 메모태그 리스트들을 가져와 반복으로 돌려서
            // 태그메모가 가진 메모와 태그가 동일한 걸 찾아서 안에 넣음

        }
        // 모든 태그 없음
        if (name == null) {

            tupleMemoTagList = query.select(memo.keyword, memo.content, tag.name)
                    .from(tagMemo)
                    .leftJoin(tagMemo.memo, memo)
                    .leftJoin(tagMemo.tag, tag)
                    // 메모->유저, 메모태그->메모, 태그->태그이름
                    .where(memo.user.id.eq(id), tag.name.isNull())
                    .orderBy(memo.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

        }
        List<MemoListDto> resultMemoTagList = new ArrayList<>();

        for (Tuple tuple : tupleMemoTagList) {
            String keyword = tuple.get(memo.keyword);
            String content = tuple.get(memo.content);
            String tag = tuple.get(QTag.tag.name);

            // MemoListDto 객체 생성 및 리스트에 추가
            MemoListDto dto = MemoListDto.builder()
                    .keyword(keyword)
                    .content(content)
                    .build();
            dto.getTag().add(tag);
            resultMemoTagList.add(dto);
        }

        return resultMemoTagList;

    }

    public void deleteTag(Long id, String name) throws MemoNotFoundException, UserNotFoundException {
        User user = em.find(User.class, id);
        if (user == null) {
            log.error("유저가 없음 = {}", id);
            throw new UserNotFoundException("유저가 없습니다.");
        }

        Tag findTag = tagRepository.findByNameAndUser(name, user).orElse(null);

        if (findTag == null) {
            log.error("해당 태그는 없음 = {}", name);
            throw new MemoNotFoundException("해당 메모는 없습니다.");
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
