package com.mega._NY.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega._NY.auth.config.details.PrincipalDetails;
import com.mega._NY.auth.dto.LoginDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.jwt.JwtToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Log4j2
public class JWTLoginFilter  extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtToken jwtToken;


    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDTO loginDto = objectMapper.readValue(request.getInputStream(), LoginDTO.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        log.info("authentication = {}", authenticationToken);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult ) throws IOException, ServletException {
        log.info("로그인 성공");
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        User user = principal.getUser();
//                log.warn("닉네임 = {}",);
        String accessToken = jwtToken.delegateAccessToken(user); //유저정보를 이용해 토큰생성
        String refreshToken = jwtToken.delegateRefreshToken(user);//리프레시 토큰 생성

        response.setHeader("Authorization", "Bearer " + accessToken); // 응답 헤더에 토큰을 담는다.
        response.setHeader("Refresh", refreshToken); //응답 헤더에 리프레시 토큰을 담는다.
        response.setHeader("userId", String.valueOf(user.getUserId()));

        if(user.getNickName() != null){
            response.getWriter().write("로그인완료");
            return;
        }

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

}
