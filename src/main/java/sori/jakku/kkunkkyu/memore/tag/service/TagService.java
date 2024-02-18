package sori.jakku.kkunkkyu.memore.tag.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sori.jakku.kkunkkyu.memore.common.exception.BadRequestException;
import sori.jakku.kkunkkyu.memore.common.exception.Exception;
import sori.jakku.kkunkkyu.memore.common.web.WebUseCase;
import sori.jakku.kkunkkyu.memore.tag.domain.Tag;
import sori.jakku.kkunkkyu.memore.tag.mapper.TagMapper;
import sori.jakku.kkunkkyu.memore.tagmemo.repository.TagQueryRepository;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.tag.dto.MainTagSaveDto;
import sori.jakku.kkunkkyu.memore.tag.dto.TagDto;
import sori.jakku.kkunkkyu.memore.tag.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.user.service.UserUseCase;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService implements TagUseCase {

    private final UserUseCase userUseCase;
    private final WebUseCase webUseCase;
    private final TagQueryRepository tagQueryRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    @Transactional
    public MainTagSaveDto writeForMain(HttpServletRequest request, MainTagSaveDto mainTagSaveDto) {
        Long id = webUseCase.getIdInHeader(request);
        User user = userUseCase.findById(id);
        if (user == null) {
            log.error("id 에 해당하는 유저 없음 = {}", id);
            throw new BadRequestException(Exception.USER_NOT_FOUND);
        }

        tagRepository.save(tagMapper.toEntity(user, mainTagSaveDto.getTagA()));
        tagRepository.save(tagMapper.toEntity(user, mainTagSaveDto.getTagB()));
        tagRepository.save(tagMapper.toEntity(user, mainTagSaveDto.getTagC()));

        return mainTagSaveDto;
    }

    @Override
    public void writeTag(HttpServletRequest request, TagDto dto) {
        Long id = webUseCase.getIdInHeader(request);
        User user = userUseCase.findById(id);

        Tag findTag = tagRepository.findByNameAndUser(dto.getName(), user).orElse(null);

        if (findTag != null) {
            log.error("태그 중복 = {}", dto.getName());
            throw new BadRequestException(Exception.DUPLICATED_TAG);
        }

        tagRepository.save(tagMapper.toEntity(user, dto));
    }

    @Override
    @Transactional
    public void deleteTag(HttpServletRequest request, TagDto tagDto) {
        Long id = webUseCase.getIdInHeader(request);
        User user = userUseCase.findById(id);

        Tag tag = tagRepository.findByNameAndUser(tagDto.getName(), user).orElseThrow(
                () -> new BadRequestException(Exception.TAG_NOT_FOUND)
        );
        tagRepository.delete(tag);
    }

    @Override
    public List<String> tagList(HttpServletRequest request) {
        Long id = webUseCase.getIdInHeader(request);
        User user = userUseCase.findById(id);

        return tagQueryRepository.findAllTag(user);
    }
}
