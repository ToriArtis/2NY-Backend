package com.mega._NY.auth.config;

import com.mega._NY.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Log4j2
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; // JWT 인증 필터 의존성 주입

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> {})
                // CSRF 보호 비활성화
                .csrf(csrf -> csrf.disable())

                // HTTP 요청에 대한 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().permitAll()
                )

                // HTTP 기본 인증 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())

                // 세션 관리 설정을 무상태(stateless)로 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // JWT 인증 필터를 CORS 필터 이후에 추가
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);



        // 설정된 SecurityFilterChain 반환
        return http.build();
    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler(){
//        return new CustomSocialLoginSuccessHandler(passwordEncoder);
//    }
}
