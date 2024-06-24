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
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(authenticationManager, userRepository);
        jwtLoginFilter.setFilterProcessesUrl("/users/login");
        jwtLoginFilter.setAuthenticationFailureHandler(new UserAuthFailureHandler());
        jwtLoginFilter.setAuthenticationSuccessHandler(new UserAuthSuccessHandler(jwtToken));

        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtToken);

        http.addFilter(jwtLoginFilter)
                .addFilterAfter(jwtVerificationFilter, JWTLoginFilter.class);
    }
}