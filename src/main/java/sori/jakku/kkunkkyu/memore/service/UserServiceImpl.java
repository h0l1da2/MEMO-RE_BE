package sori.jakku.kkunkkyu.memore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.NotValidException;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean usernameDupl(String username) {
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
        return false;
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
         */
        if (username.length() < 3) {
            return true;
        }

        for (char c : username.toCharArray()) {
            if (!Character.isLowerCase(c) && !Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}
