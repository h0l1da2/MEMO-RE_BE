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
    public boolean usernameValid(String username) {
        return false;
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

}
