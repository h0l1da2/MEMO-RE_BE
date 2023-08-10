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
    public String creatToken(Long id, String username) {
        return tokenProvider.createToken(id, username);
    }

    @Override
    public String reCreateToken(String token) {
        Long id = tokenParser.getId(token);
        String username = tokenParser.getUsername(token);
        return tokenProvider.createToken(id, username);
    }

    @Override
    public boolean tokenValid(String token) {
        return tokenParser.isValid(token);
    }

    @Override
    public String usernameByToken(String token) {
        return tokenParser.getUsername(token);
    }

    @Override
    public Long getIdByToken(String token) {
        return tokenParser.getId(token);
    }


}
