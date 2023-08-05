package sori.jakku.kkunkkyu.memore.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.domain.dto.UserDto;
import sori.jakku.kkunkkyu.memore.exception.PasswordNotValidException;
import sori.jakku.kkunkkyu.memore.exception.UsernameDuplException;
import sori.jakku.kkunkkyu.memore.exception.UsernameNotValidException;
import sori.jakku.kkunkkyu.memore.repository.UserRepository;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

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
    public boolean pwdCheck(String password) {
        try {
            password = webService.jsonToString(password, "password");
        } catch (NullPointerException e) {
            log.info("password is Null");
            return false;
        }

        if (!pwdValid(password)) return false;

        return true;
    }

    @Override
    public UserDto signUp(UserDto userDto) throws UsernameNotValidException, PasswordNotValidException, UsernameDuplException {
        /**
         * 아이디, 패스워드 valid 확인 후 저장
         */

        if (!usernameValid(userDto.getUsername())) {
            throw new UsernameNotValidException();
        }

        if (!pwdValid(userDto.getPassword())) {
            throw new PasswordNotValidException();
        }

        User findUser = userRepository.findByUsername(userDto.getUsername());
        if (findUser != null) {
            throw new UsernameDuplException();
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(new User(userDto));
        return new UserDto(user);
    }

    @Override
    public boolean login(UserDto userDto) {

        User findUser = userRepository.findByUsername(userDto.getUsername());
        if (findUser == null) {
            return false;
        }

        return passwordEncoder.matches(userDto.getPassword(), findUser.getPassword());
    }

    private boolean usernameValid(String username) throws NullPointerException {
        /**
         * 아이디
         * - 4자 이상 ~ 15자 이하
         * - 소문자, 숫자만
         * - 공백 불허용
         */
        if (!StringUtils.isNotBlank(username)) {
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

    private boolean pwdValid(String password) throws NullPointerException {
        /**
         * 패스워드
         * - 6자 이상 ~ 15자 이하
         * - 공백 불허용
         * - 대소문자, 숫자
         */
        if (!StringUtils.isNotBlank(password)) {
            return false;
        }

        if (password.length() < 6 || password.length() > 15) {
            return false;
        }

        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
