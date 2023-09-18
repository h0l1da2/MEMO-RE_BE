package sori.jakku.kkunkkyu.memore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.Tag;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;
import sori.jakku.kkunkkyu.memore.domain.dto.TagWriteDto;
import sori.jakku.kkunkkyu.memore.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.repository.CustomTagMemoRepository;
import sori.jakku.kkunkkyu.memore.repository.TagRepository;
import sori.jakku.kkunkkyu.memore.service.inter.TagService;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final UserService userService;
    private final WebService webService;
    private final CustomTagMemoRepository tagMemoRepository;
    private final TagRepository tagRepository;

    @Override
    public String writeForMain(Long id, TagDto tagDto) throws UserNotFoundException {
        /**
         * 유저 가져오기
         * tag DB 에 추가
         * TagDto -> Json
         * return Json
         */

        User user = userService.userById(id);
        if (user == null) {
            log.error("id 에 해당하는 유저 없음 = {}", id);
            throw new UserNotFoundException("유저가 없습니다.");
        }

        tagMemoRepository.saveTagMain(user, tagDto);

        return webService.objectToJson(tagDto);
    }

    @Override
    public String writeTag(Long id, String name) throws UserNotFoundException, DuplicateMemoException, ConditionNotMatchException {
        /**
         * 유저 찾고 이름 검증 후, 태그 생성 및 DB INSERT
         */

        if (name == null || name.isBlank()
                || name.length() > 20
                || !name.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*")) {
            log.error("태그 이름 형식 다름 = {}", name);
            throw new ConditionNotMatchException("태그 이름 형식이 맞지 않음");
        }

        User user = userService.userById(id);
        if (user == null) {
            log.error("id 에 해당하는 유저 없음 = {}", id);
            throw new UserNotFoundException("유저가 없습니다.");
        }

        Tag findTag = tagRepository.findByNameAndUser(name, user).orElse(null);

        if (findTag != null) {
            log.error("태그 중복 = {}", name);
            throw new DuplicateMemoException("태그가 중복입니다.");
        }

        Tag tag = tagRepository.save(new Tag(user, name));

        return tag.getName();
    }

    @Override
    public void deleteTag(Long id, TagWriteDto tagWriteDto) throws UserNotFoundException, MemoNotFoundException {
        tagMemoRepository.deleteTag(id, tagWriteDto.getName());
    }

    @Override
    public List<String> tagList(Long id) throws UserNotFoundException {
        User user = userService.userById(id);

        if (user == null) {
            log.error("id 에 해당하는 유저 없음 = {}", id);
            throw new UserNotFoundException("유저가 없습니다.");
        }

        return tagMemoRepository.findAllTag(id);

    }
}
