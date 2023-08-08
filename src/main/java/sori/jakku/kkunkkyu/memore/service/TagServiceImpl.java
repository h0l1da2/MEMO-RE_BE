package sori.jakku.kkunkkyu.memore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.TagDto;
import sori.jakku.kkunkkyu.memore.exception.ConditionNotMatchException;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.repository.CustomTagRepository;
import sori.jakku.kkunkkyu.memore.service.inter.TagService;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final UserService userService;
    private final WebService webService;
    private final CustomTagRepository customTagRepository;
    
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

        customTagRepository.saveForMain(user, tagDto);

        return webService.objectToJson(tagDto);
    }

    private boolean validTag(TagDto tagDto) throws ConditionNotMatchException {
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
