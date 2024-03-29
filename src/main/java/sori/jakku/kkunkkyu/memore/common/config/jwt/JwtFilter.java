package sori.jakku.kkunkkyu.memore.common.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenUseCase tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * 토큰 존재 여부 확인 (헤더)
         * 토큰 유효 확인
         * 토큰 다시 생성
         * 토큰 쿠키 넣기
         * 어썬티케이션 생성
         * 필터 체인
         */

        // 토큰 존재 여부 확인
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 'Bearer' + ' ' 띄어쓰기 포함해서 제거
        token = token.substring(JwtToken.BEARER.getValue().length() + 1);

        // 토큰 유효 확인
        boolean valid = tokenService.tokenValid(token);

        if (!valid) {
            log.debug("토큰 일치하지 않음.");
            HttpSession session = request.getSession(false);
            session.invalidate();
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 재생성
        String reCreateToken = tokenService.reCreateToken(token);
        // 헤더 셋팅
        setTokenAtHeader(response, reCreateToken);

        // Authentication 생성
        setAuthenticationAtSecurityContextHolder(token);

        filterChain.doFilter(request, response);
    }

    private void setTokenAtHeader(HttpServletResponse response, String reCreateToken) {
        response.setHeader(HttpHeaders.AUTHORIZATION, JwtToken.BEARER.getValue() + " " + reCreateToken);
    }

    private void setAuthenticationAtSecurityContextHolder(String token) {
        String username = tokenService.usernameByToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
