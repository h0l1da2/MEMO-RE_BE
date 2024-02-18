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

    private final Key key;

    public TokenProvider(@Value("${token.secret.key}") String secretKey) {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Long id, String username) {
        Claims claims = getClaims(id, username);

        return Jwts.builder()
                .setHeaderParam(JwtToken.TYP.getValue(), JwtToken.BEARER.getValue())
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getClaims(Long id, String username) {
        Date now = new Date();

        Claims claims = Jwts.claims()
                .setSubject(JwtToken.ACCESS_TOKEN.getValue())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime));

        claims.put(JwtToken.ID.getValue(), id.toString());
        claims.put(JwtToken.USERNAME.getValue(), username);
        claims.put(JwtToken.ROLE.getValue(), JwtToken.ROLE_USER.getValue());
        return claims;
    }
}
