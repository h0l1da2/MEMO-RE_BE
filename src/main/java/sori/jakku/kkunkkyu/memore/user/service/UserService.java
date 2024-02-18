package sori.jakku.kkunkkyu.memore.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sori.jakku.kkunkkyu.memore.common.converter.JsonStringConverter;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.user.dto.UserDto;
import sori.jakku.kkunkkyu.memore.common.exception.UsernameDuplException;
import sori.jakku.kkunkkyu.memore.user.repository.UserRepository;

import javax.security.auth.login.LoginException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean usernameDupl(String username) throws UsernameDuplException {

        try {
            username = JsonStringConverter.jsonToString(username, "username");
        } catch (NullPointerException e) {
            log.info("username is Null");
            return false;
        }

        // 아이디 조건 검증
        if (!usernameValid(username)){
            return false;
        }

        // 아이디 중복 검증
        User user = userRepository.findByUsername(username);
        if (user != null) {
            log.error("아이디 중복 = {}", username);
            throw new UsernameDuplException("아이디가 중복입니다.");
        }

        return true;
    }

    @Override
    public UserDto signUp(UserDto userDto) throws UsernameDuplException {

        User findUser = userRepository.findByUsername(userDto.getUsername());
        if (findUser != null) {
            log.error("아이디 중복 = {}", findUser.getUsername());
            throw new UsernameDuplException("아이디가 중복입니다.");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(new User(userDto));
        return new UserDto(user);
    }

    @Override
    public User login(UserDto userDto) throws LoginException {

        User findUser = userRepository.findByUsername(userDto.getUsername());
        if (findUser == null) {
            log.error("유저를 찾을 수 없음 = {}", userDto.getUsername());
            throw new LoginException("유저 이름을 찾을 수 없습니다.");
        }

        boolean matches = passwordEncoder.matches(userDto.getPassword(), findUser.getPassword());
        if (!matches) {
            log.error("비밀번호가 다름 = {}", findUser.getPassword());
            throw new LoginException("비밀번호가 다릅니다.");
        }

        return findUser;
    }

    @Override
    public User userByUsername(String username) {
        return userRepository.findByUsername(username);
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
