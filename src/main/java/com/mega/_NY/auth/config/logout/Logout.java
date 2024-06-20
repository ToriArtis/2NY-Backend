package com.mega._NY.auth.config.logout;

import com.mega._NY.auth.config.RedisConfig;
import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.jwt.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class Logout {

    private final JwtToken jwtToken;
    private final SecretKey secretKey;
    private final RedisConfig redis;
    public static final String REDIS_KEY_PREFIX = "logoutToken ";

    public void doLogout( HttpServletRequest request ){
        String jws = jwtToken.extractJws(request);
        Key key = secretKey.getSecretKey(secretKey.getBaseKey());
        Jws<Claims> claims = jwtToken.getClaims(jws, key);
        Date expiration = claims.getBody().getExpiration();

        if(notLoginToken(request).equals(Boolean.TRUE)){
            redis.redisTemplate().opsForValue()
                    .set(REDIS_KEY_PREFIX + jws, "token", expiration.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            log.info("로그아웃 완료");
        }
    }

    private Boolean notLoginToken( HttpServletRequest request ){
        String jws = jwtToken.extractJws(request);
        Key key = secretKey.getSecretKey(secretKey.getBaseKey());
        Jws<Claims> claims = jwtToken.getClaims(jws, key);
        boolean notExpired = claims.getBody().getExpiration().after(new Date());

        if(redis.redisTemplate().opsForValue().get(REDIS_KEY_PREFIX + jws) != null){
            return false;
        }
        return notExpired;
    }
}
