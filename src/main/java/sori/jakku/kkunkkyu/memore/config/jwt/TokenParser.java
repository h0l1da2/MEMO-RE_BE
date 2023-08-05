package sori.jakku.kkunkkyu.memore.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// 토큰 파싱 클래스
@Slf4j
@Component
public class TokenParser {
    private final JwtParser jwtParser;
    public TokenParser(@Value("${token.secret.key}") String secretKey) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public boolean isValid(String token) {
        Claims claims = getClaims(token);
        if (claims.getSubject() != null) {
            return !claims.getExpiration().before(new Date());
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    private Claims getClaims(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims;
    }
}
