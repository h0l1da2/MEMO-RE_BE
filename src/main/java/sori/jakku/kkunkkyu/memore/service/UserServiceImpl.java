package sori.jakku.kkunkkyu.memore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.UsernameDuplException;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

import javax.security.auth.login.LoginException;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WebService webService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean usernameDupl(String username) throws UsernameDuplException {

        try {
            username = webService.jsonToString(username, "username");
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
            throw new UsernameDuplException();
        }

        return true;
    }

    @Override
    public UserDto signUp(UserDto userDto) throws UsernameDuplException {

        User findUser = userRepository.findByUsername(userDto.getUsername());
        if (findUser != null) {
            throw new UsernameDuplException();
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(new User(userDto));
        return new UserDto(user);
    }

    @Override
    public User login(UserDto userDto) throws LoginException {

        User findUser = userRepository.findByUsername(userDto.getUsername());
        if (findUser == null) {
            throw new LoginException("유저 이름 찾을 수 없음");
        }

        boolean matches = passwordEncoder.matches(userDto.getPassword(), findUser.getPassword());
        if (!matches) {
            throw new LoginException("비밀번호 다름");
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
