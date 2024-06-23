package com.mega._NY.auth.config;

import com.mega._NY.auth.config.handler.UserAccessDeniedHandler;
import com.mega._NY.auth.config.handler.UserAuthenticationEntryPoint;
import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.jwt.SecretKey;
import com.mega._NY.auth.jwt.filter.JwtAuthenticationFilter;
import com.mega._NY.auth.jwt.filter.JwtVerificationFilter;
import com.mega._NY.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@Log4j2
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final JwtToken jwtToken;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey secretKey;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                })
                .headers(httpSecurityHeadersConfigurer
                        -> httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()));


        http.sessionManagement(httpSecuritySessionManagementConfigurer
                -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable());
        http.httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable());
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        http.cors(httpSecurityCorsConfigurer
                -> httpSecurityCorsConfigurer.configurationSource(request
                -> new CorsConfiguration().applyPermitDefaultValues()));
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new UserAccessDeniedHandler());
            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new UserAuthenticationEntryPoint());
                });

//        http.apply(new CustomFilterConfigurer(jwtToken,userRepository)); -> 이것만 해결하면 됨~
        http.addFilterBefore(new JwtAuthenticationFilter(jwtToken, userDetailsService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtVerificationFilter(jwtToken), JwtAuthenticationFilter.class);

        return http.build();
    }
}