package com.mega._NY.login.jwt;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.jwt.SecretKey;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@Log4j2
@SpringBootTest
public class JwtTokenTest {
    private String baseKey = "sdfsdfsdfesdfdf11113456645454534545451";

    @Autowired
    private JwtToken jwtToken;
    private Key key;
    private User user;

    @Autowired
    private SecretKey secretKey;

    @BeforeEach
    void init(){
        key = secretKey.getSecretKey(baseKey);

        user = User.builder().id(1L).nickName("test").email("test1@gmail.com").build();
    }

    @Test
    void acccess_tioken_create() throws Exception{
        String accessToken = getAccessToken(key, user, 20, Calendar.HOUR);
        log.info("accessToken create : "+accessToken);
        assertThat(accessToken).isNotNull();
    }

    @Test
    void token_finish() throws Exception{

        String accessToken = getAccessToken(key, user, 2, Calendar.SECOND);
        TimeUnit.MILLISECONDS.sleep(2000);

        // 시간 초과 에러가 나는 것이 당연하기 때문에 아래 try-catch문을 좀 추가해놨어요 (''
//        try {
//            verifySignature(accessToken, key);
//        } catch (ExpiredJwtException e) {
//            log.error("ExpiredJwtException thrown as expected: " + e.getMessage());
//            log.error("JWT expired at: " + e.getClaims().getExpiration());
//            log.error("Current time: " + new java.util.Date());
//            throw e;
//        }


        assertThatExceptionOfType(ExpiredJwtException.class).isThrownBy(()-> verifySignature(accessToken, key));

    }
    @Test
    void jwt_verify() throws Exception{
        String accessToken = getAccessToken(key, user, 2, Calendar.MINUTE);
        log.info("accessToken : "+accessToken);

        assertThatCode(() -> verifySignature(accessToken, key)).doesNotThrowAnyException();
        log.info("test passed");

    }

    private String getAccessToken( Key key, User user, int time, int timeUnit ){
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getEmail());
        claims.put("displayName", user.getNickName());

        String subject = "test token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, time);
        Date expiration = calendar.getTime();

        String acessToken = jwtToken.createAccessToken(claims, subject, expiration, key);
        return acessToken;
    }

    private void verifySignature(String accessToken, Key key) {

        Jwts.parser().setSigningKey(key).build().parseClaimsJws(accessToken);

    }
}
