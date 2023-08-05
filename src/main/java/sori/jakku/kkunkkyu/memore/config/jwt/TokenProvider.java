package sori.jakku.kkunkkyu.memore.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// 토큰 생성 클래스
@Component
@PropertySource("classpath:application.yml")
public class TokenProvider {

    // 토큰 유효 시간
    @Value("${token.valid.time}")
    private int tokenValidTime;

    private Key key;

    public TokenProvider(@Value("${token.secret.key}") String secretKey) {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username) {
        Claims claims = getClaims(username);

        return Jwts.builder()
                .setHeaderParam("typ", "Bearer")
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getClaims(String username) {
        Date now = new Date();

        Claims claims = Jwts.claims()
                .setSubject("accessToken")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime));

        claims.put("username", username);
        return claims;
    }
}
