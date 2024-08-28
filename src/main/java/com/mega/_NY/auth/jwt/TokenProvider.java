package com.mega._NY.auth.jwt;

import com.mega._NY.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import java.security.Key;

@Component
public class TokenProvider {

    // Access 토큰을 위한 암호화 키
    private final Key accessKey;
    // Refresh 토큰을 위한 암호화 키
    private final Key refreshKey;

    // 생성자: 암호화 키 초기화
    public TokenProvider() {
        // HS512 알고리즘을 사용하여 안전한 키 생성
        this.accessKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    // Access 토큰 생성 메서드
    public String createAccessToken(User userEntity) {
        // 현재 시간으로부터 30분 후의 만료 시간 설정
        Date expiryDate = Date.from(Instant.now().plus(30, ChronoUnit.MINUTES));

        // JWT 빌더를 사용하여 토큰 생성
        return Jwts.builder()
                .setSubject(userEntity.getEmail())  // 사용자 이메일을 subject로 설정
                .setIssuer("demo app")  // 발행자 설정
                .setIssuedAt(new Date())  // 발행 시간 설정
                .setExpiration(expiryDate)  // 만료 시간 설정
                .signWith(accessKey, SignatureAlgorithm.HS512)  // 암호화 키와 알고리즘으로 서명
                .compact();  // 토큰 생성
    }

    // Refresh 토큰 생성 메서드
    public String createRefreshToken(User userEntity) {
        // 현재 시간으로부터 7일 후의 만료 시간 설정
        Date expiryDate = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));

        // JWT 빌더를 사용하여 토큰 생성 (Access 토큰과 유사하지만 만료 시간이 더 김)
        return Jwts.builder()
                .setSubject(userEntity.getEmail())
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Access 토큰 검증 및 사용자 ID 추출 메서드
    public String validateAndGetUserId(String token) {
        // 토큰을 파싱하여 Claims(페이로드) 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(accessKey)  // 암호화 키 설정
                .build()
                .parseClaimsJws(token)  // 토큰 파싱
                .getBody();  // Claims 얻기

        // subject(여기서는 사용자 이메일)를 반환
        return claims.getSubject();
    }

    // Refresh 토큰 검증 및 사용자 ID 추출 메서드
    public String validateAndGetUserIdFromRefreshToken(String token) {
        // Access 토큰과 유사하지만 refresh 키를 사용
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // 토큰 만료 여부 확인 메서드
    public boolean isTokenExpired(String token) {
        try {
            // 토큰에서 Claims 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // 현재 시간과 비교하여 만료 여부 반환
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // 예외 발생 시 만료된 것으로 간주
            return true;
        }
    }


    public long getTokenRemainingTime(String token, boolean isRefreshToken) {
        try {
            Key key = isRefreshToken ? refreshKey : accessKey;
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            Date now = new Date();

            return Math.max(0, expiration.getTime() - now.getTime());
        } catch (Exception e) {
            return -1; // 토큰이 유효하지 않거나 파싱 중 오류가 발생한 경우
        }
    }
}