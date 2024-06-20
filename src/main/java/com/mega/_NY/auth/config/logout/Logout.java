package com.mega._NY.auth.config.logout;

import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.jwt.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Log4j2
@Component
@RequiredArgsConstructor
public class Logout {

    private final JwtToken jwtToken;
    private final SecretKey secretKey;

    public void doLogout(HttpServletRequest request) {
        String jws = jwtToken.extractJws(request);
        Key key = secretKey.getSecretKey(secretKey.getBaseKey());
        Jws<Claims> claims = jwtToken.getClaims(jws, key);
        Date expiration = claims.getBody().getExpiration();

        if (notLoginToken(request).equals(Boolean.TRUE)) {
            log.info("로그아웃 완료");
        }
    }

    private Boolean notLoginToken(HttpServletRequest request) {
        String jws = jwtToken.extractJws(request);
        Key key = secretKey.getSecretKey(secretKey.getBaseKey());
        Jws<Claims> claims = jwtToken.getClaims(jws, key);
        boolean notExpired = claims.getBody().getExpiration().after(new Date());

        return notExpired;
    }
}