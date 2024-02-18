package sori.jakku.kkunkkyu.memore.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sori.jakku.kkunkkyu.memore.common.config.jwt.JwtToken;
import sori.jakku.kkunkkyu.memore.common.config.jwt.TokenUseCase;
import sori.jakku.kkunkkyu.memore.common.converter.JsonStringConverter;
import sori.jakku.kkunkkyu.memore.common.exception.BadRequestException;
import sori.jakku.kkunkkyu.memore.common.exception.Exception;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.user.dto.UserDto;
import sori.jakku.kkunkkyu.memore.user.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final TokenUseCase tokenUseCase;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean usernameDupl(String username) {
        username = JsonStringConverter.jsonToString(Objects.requireNonNull(username), "username");

        // 아이디 조건 검증
        if (!usernameValid(username)){
            return false;
        }

        // 아이디 중복 검증
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            log.error("아이디 중복 = {}", username);
            throw new BadRequestException(Exception.DUPLICATED_USERNAME);
        }

        return true;
    }

    @Override
    public UserDto signUp(UserDto userDto) {
        User findUser = userRepository.findByUsername(userDto.getUsername())
                .orElse(null);

        if (findUser != null) {
            log.error("아이디 중복 = {}", findUser.getUsername());
            throw new BadRequestException(Exception.DUPLICATED_USERNAME);
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(new User(userDto));

        return new UserDto(user);
    }

    @Override
    public Map<String, String> login(UserDto userDto) {
        User findUser = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new BadRequestException(Exception.USER_NOT_FOUND));

        boolean matches = passwordEncoder.matches(userDto.getPassword(), findUser.getPassword());
        if (!matches) {
            log.error("비밀번호가 다름 = {}", findUser.getUsername());
            throw new BadRequestException(Exception.USER_NOT_FOUND);
        }

        Map<String, String> resultToken = new HashMap<>();
        resultToken.put(JwtToken.ACCESS_TOKEN.getValue(), tokenUseCase.creatToken(findUser.getId(), findUser.getUsername()));
        return resultToken;
    }

    @Override
    public User userById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private boolean usernameValid(String username) throws NullPointerException {
        /**
         * 아이디
         * - 4자 이상 ~ 15자 이하
         * - 소문자, 숫자만
         * - 공백 불허용
         */
        if (!StringUtils.hasText(username)) {
            return false;
        }

        if (username.length() < 4 || username.length() > 15) {
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
