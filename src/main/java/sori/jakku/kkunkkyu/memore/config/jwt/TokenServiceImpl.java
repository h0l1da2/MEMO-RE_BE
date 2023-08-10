package sori.jakku.kkunkkyu.memore.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    @Override
    public String creatToken(Long id) {
        // 토큰 생성 Provider
        return tokenProvider.createToken(id);
    }

    @Override
    public String reCreateToken(String token) {
        String id = tokenParser.getId(token);
        return tokenProvider.createToken(Long.parseLong(id));
    }

    @Override
    public boolean tokenValid(String token) {
        return tokenParser.isValid(token);
    }

    @Override
    public String usernameByToken(String token) {
        return tokenParser.getId(token);
    }


}
