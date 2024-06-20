package com.mega._NY.auth.jwt;

import com.mega._NY.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtToken {

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    private final SecretKey secretKey;

    public String createAccessToken(Map<String, Object> claims, String subject, Date expiration, Key key) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(expiration)
                .setIssuedAt(Calendar.getInstance().getTime())
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String subject, Date expiration, Key key) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiration)
                .setIssuedAt(Calendar.getInstance().getTime())
                .signWith(key)
                .compact();
    }

    // 수정된 부분: "Authorization" 헤더가 없는 경우 처리
    public String extractJws(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.replace("Bearer ", "");
    }

    public String delegateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getEmail());
        claims.put("roles", user.getRoles());

        String subject = user.getEmail();
        Date expiration = getExpiration(accessTokenExpirationMinutes);
        Key key = secretKey.getSecretKey(secretKey.getBaseKey());

        return createAccessToken(claims, subject, expiration, key);
    }

    public String delegateRefreshToken(User user) {
        String subject = user.getEmail();
        Date expiration = getExpiration(refreshTokenExpirationMinutes);
        Key key = secretKey.getSecretKey(secretKey.getBaseKey());

        return createRefreshToken(subject, expiration, key);
    }

    private Date getExpiration(int tokenExpirationMinutes) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, tokenExpirationMinutes);
        return instance.getTime();
    }

    // 수정된 부분: `Jwts.parserBuilder()` 사용
    public Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = extractJws(request);
        if (jws == null) {
            return null;
        }
        Key key = secretKey.getSecretKey(secretKey.getBaseKey());

        return getClaims(jws, key).getBody();
    }

    // JWS의 클레임 추출
    public Jws<Claims> getClaims(String jws, Key key) {
        return Jwts.parser()
                .setSigningKey(key)
                .build().parseClaimsJws(jws);
    }
}
