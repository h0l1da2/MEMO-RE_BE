package sori.jakku.kkunkkyu.memore.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.common.config.jwt.JwtToken;
import sori.jakku.kkunkkyu.memore.common.config.jwt.TokenUseCase;
import sori.jakku.kkunkkyu.memore.common.exception.BadRequestException;
import sori.jakku.kkunkkyu.memore.common.exception.Exception;
import sori.jakku.kkunkkyu.memore.user.domain.User;
import sori.jakku.kkunkkyu.memore.user.dto.LoginDto;
import sori.jakku.kkunkkyu.memore.user.dto.SignUpDto;
import sori.jakku.kkunkkyu.memore.user.mapper.UserMapper;
import sori.jakku.kkunkkyu.memore.user.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final TokenUseCase tokenUseCase;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void signUp(SignUpDto dto) {
        User findUser = userRepository.findByUsername(dto.username())
                .orElse(null);

        if (findUser != null) {
            log.error("아이디 중복 = {}", findUser.getUsername());
            throw new BadRequestException(Exception.DUPLICATED_USERNAME);
        }

        String encodePwd = passwordEncoder.encode(dto.password());
        User user = userMapper.toEntity(dto);
        user.encodedPassword(encodePwd);

        userRepository.save(user);
    }

    @Override
    public Map<String, String> login(LoginDto loginDto) {
        User findUser = userRepository.findByUsername(loginDto.username())
                .orElseThrow(() -> new BadRequestException(Exception.USER_NOT_FOUND));

        boolean matches = passwordEncoder.matches(loginDto.password(), findUser.getPassword());
        if (!matches) {
            log.error("비밀번호가 다름 = {}", findUser.getUsername());
            throw new BadRequestException(Exception.USER_NOT_FOUND);
        }

        Map<String, String> resultToken = new HashMap<>();
        resultToken.put(JwtToken.ACCESS_TOKEN.getValue(), tokenUseCase.creatToken(findUser.getId(), findUser.getUsername()));
        return resultToken;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BadRequestException(Exception.USER_NOT_FOUND));
    }
}
