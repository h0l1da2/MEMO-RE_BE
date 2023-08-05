package sori.jakku.kkunkkyu.memore.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.domain.User;
import sori.jakku.kkunkkyu.memore.service.inter.UserService;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserService userService;

    @Override
    public String creatToken(String username) {
        User user = userService.getUserByUsername(username);

        // 토큰 생성 Provider
        return null;
    }
}
