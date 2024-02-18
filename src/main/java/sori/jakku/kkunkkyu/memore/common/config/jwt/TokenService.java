package sori.jakku.kkunkkyu.memore.common.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService implements TokenUseCase {

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
