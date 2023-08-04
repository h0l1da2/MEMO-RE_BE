package sori.jakku.kkunkkyu.memore.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.NotValidException;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WebService webService;

    @Override
    public boolean usernameDupl(String username) {
        username = webService.jsonToString(username, "username");

        // 아이디 조건 검증
        if (!usernameValid(username)){
            return false;
        }

        // 아이디 중복 검증
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean pwdValid(String password) {
        password = webService.jsonToString(password, "password");
        /**
         * 패스워드
         * - 6자 이상
         * - 공백 불허용
         * - 대소문자, 숫자
         */
        if (!StringUtils.isNotBlank(password)) {
            return false;
        }

        if (password.length() < 5) {
            return false;
        }

        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public UserDto signUp(UserDto userDto) throws NotValidException {
        /**
         * 아이디, 패스워드 valid 확인 후 저장
         */
        User user = userRepository.save(new User(userDto));
        return new UserDto(user);
    }

    private boolean usernameValid(String username) {
        /**
         * 아이디
         * - 4자 이상
         * - 소문자, 숫자만
         * - 공백 불허용
         */
        if (!StringUtils.isNotBlank(username)) {
            return false;
        }

        if (username.length() < 3) {
            return false;
        }

        for (char c : username.toCharArray()) {
            if (!Character.isLowerCase(c) && !Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
