package com.mega._NY.auth.config;

import com.mega._NY.auth.config.handler.UserAuthFailureHandler;
import com.mega._NY.auth.config.handler.UserAuthSuccessHandler;
import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.jwt.SecretKey;
import com.mega._NY.auth.jwt.filter.JWTLoginFilter;
import com.mega._NY.auth.jwt.filter.JwtVerificationFilter;
import com.mega._NY.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {


    private final JwtToken jwtToken;
    private final UserRepository userRepository;

    @Override
    public void configure( HttpSecurity builder ) throws Exception{
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(authenticationManager, userRepository);//필터 실행
        jwtLoginFilter.setFilterProcessesUrl("/users/login"); //로그인 디폴트 url

        jwtLoginFilter.setAuthenticationFailureHandler(new UserAuthFailureHandler());//로그인 실패시 핸들러 설정
        jwtLoginFilter.setAuthenticationSuccessHandler(new UserAuthSuccessHandler(jwtToken));//로그인 성공시 핸들러 설정

        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtToken); //jwt인증 필터 설정

        builder.addFilter(jwtLoginFilter) //로그인 필터 추가
                .addFilterAfter(jwtVerificationFilter, JWTLoginFilter.class);//로그인 필터가 실행된 바로 다음 jwt인증 필터 실행
    }
}
