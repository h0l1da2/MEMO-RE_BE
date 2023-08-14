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

import java.util.ArrayList;
import java.util.List;

import static sori.jakku.kkunkkyu.memore.domain.QMemo.*;
import static sori.jakku.kkunkkyu.memore.domain.QTag.*;

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
        Memo memo = new Memo(user);
        memo.writeOnlyKeyword(memoWriteDto.getKeyword());
        if (memoWriteDto.getContent() != null) {
            memo.writeOnlyContent(memoWriteDto.getContent());
        }

        em.persist(memo);
        // 태그가 있을 경우, 태그 추가 및 불러오기
        if (memoWriteDto.getTag() != null) {
            for (String name : memoWriteDto.getTag()) {
                Tag tag = new Tag(user, memo,name);
                    em.persist(tag);

            }
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
                    // 태그가 이미 있는 거면 놔두고, 없으면 태그메모테이블과 태그에 새로 추가
                    if (value == true) {
                        Tag findTag = tagRepository.findByName(key).orElse(null);
                        if (findTag == null) {
                            tagRepository.save(new Tag(memo.getUser(), memo, key));
                        }
                    }
                }
                );

    }

    public void deleteMemo(Memo memo) {
        memo = em.merge(memo);
        em.remove(memo);
    }

    public List<MemoListDto> findAllForList(Long id, Pageable pageable, String name) {
        System.out.println("시작");
        List<Memo> memoList = query.select(memo)
                .from(memo, tag)
                .where(tag.name.eq(name), tag.memo.user.id.eq(id))
                .orderBy(tag.memo.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Tag> tagList = query.select(tag)
                .from(tag)
                .where(tag.memo.in(memoList))
                .orderBy(tag.memo.id.desc())
                .fetch();

        List<MemoListDto> list = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        for (Memo memo : memoList) {
            for (Tag tag : tagList) {
                if (memo == tag.getMemo()) {
                    tags.add(tag.getName());
                }
            }
            list.add(new MemoListDto(memo.getKeyword(), memo.getContent(), tags));
        }

        return list;

    }

}
