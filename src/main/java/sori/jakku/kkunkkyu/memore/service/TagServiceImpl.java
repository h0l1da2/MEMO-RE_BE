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

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final UserService userService;
    private final WebService webService;
    private final CustomTagMemoRepository customTagMemoRepository;
    private final TagRepository tagRepository;

    @Override
    public String writeForMain(Long id, TagDto tagDto) throws UserNotFoundException, ConditionNotMatchException {
        /**
         * 유저 가져오기
         * tag 길이 검사 (1~10자)
         * tag DB 에 추가
         * TagDto -> Json
         * return Json
         */

        if (!validTag(tagDto)) {
            throw new ConditionNotMatchException("태그 조건이 맞지 않음");
        }

        User user = userService.userById(id);
        if (user == null) {
            throw new UserNotFoundException("유저가 없음");
        }

        customTagMemoRepository.saveTagMain(user, tagDto);

        return webService.objectToJson(tagDto);
    }

    @Override
    public String writeTag(Long id, String name) throws UserNotFoundException, ConditionNotMatchException, DuplicateMemoException {
        /**
         * 유저 찾고 이름 검증 후, 태그 생성 및 DB INSERT
         */
        User user = userService.userById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }

        // 한글이나 영어가 포함되어있지 않다면
        if (20 < name.length() |
                (!name.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") &&
                !name.matches(".*[a-zA-Z]+.*") &&
                !name.matches(".*\\d+.*"))) {
            throw new ConditionNotMatchException();
        }

        Tag findTag = tagRepository.findByName(name).orElse(null);

        if (findTag != null) {
            throw new DuplicateMemoException("태그 중복");
        }

        Tag tag = tagRepository.save(new Tag(user, name));

        return tag.getName();
    }

    @Override
    public void deleteTag(Long id, TagWriteDto tagWriteDto) throws UserNotFoundException, MemoNotFoundException {
        customTagMemoRepository.deleteTag(id, tagWriteDto.getName());
    }

    private boolean validTag(TagDto tagDto) {
        if (10 < tagDto.getTagA().length()) {
            return false;
        }
        if (10 < tagDto.getTagB().length()) {
            return false;
        }
        if (10 < tagDto.getTagC().length()) {
            return false;
        }
        return true;
    }
}
