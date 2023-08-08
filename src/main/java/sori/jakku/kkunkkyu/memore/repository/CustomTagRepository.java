package sori.jakku.kkunkkyu.memore.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.domain.Tag;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomTagRepository {

    private final TagRepository tagRepository;

    @Transactional
    public TagDto saveForMain(User user, TagDto tagDto) {
        // 트랜잭션 시작
        tagRepository.save(new Tag(user, tagDto.getTagA()));
        tagRepository.save(new Tag(user, tagDto.getTagB()));
        tagRepository.save(new Tag(user, tagDto.getTagC()));
        // 끝
        return tagDto;
    }
}
