package sori.jakku.kkunkkyu.memore.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;

    @Override
    public String creatToken(String username) {
        // 토큰 생성 Provider
        return tokenProvider.createToken(username);
    }
}
