package sori.jakku.kkunkkyu.memore.common.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.common.config.jwt.TokenUseCase;

@Service
@RequiredArgsConstructor
public class WebService implements WebUseCase {

    private final TokenUseCase tokenService;

    @Override
    public Long getIdInHeader(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        token = token.substring("Bearer ".length());
        return tokenService.getIdByToken(token);
    }
}
