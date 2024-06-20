package com.mega._NY.auth.config;

import com.mega._NY.auth.config.handler.UserAccessDeniedHandler;
import com.mega._NY.auth.config.handler.UserAuthFailureHandler;
import com.mega._NY.auth.config.handler.UserAuthSuccessHandler;
import com.mega._NY.auth.config.handler.UserAuthenticationEntryPoint;
import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.jwt.filter.JWTLoginFilter;
import com.mega._NY.auth.jwt.filter.JwtAuthenticationFilter;
import com.mega._NY.auth.jwt.filter.JwtVerificationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import javax.sql.DataSource;

@Configuration
@Log4j2
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    private final JwtToken jwtToken;
    private final RedisConfig redisConfig;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        log.info("configure -> Security FilterChain");

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basic -> basic.disable())
                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(csrf -> csrf.disable());

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(authenticationManager, jwtToken);
        jwtLoginFilter.setFilterProcessesUrl("/users/login");
        jwtLoginFilter.setAuthenticationFailureHandler(new UserAuthFailureHandler());
        jwtLoginFilter.setAuthenticationSuccessHandler(new UserAuthSuccessHandler(jwtToken));

        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(redisConfig, jwtToken);

        http.addFilter(jwtLoginFilter)
                .addFilterAfter(jwtVerificationFilter, JWTLoginFilter.class);



        // 권한 설정
        http.oauth2Login(oauth2 -> oauth2.successHandler(new UserAuthSuccessHandler(jwtToken)))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/carts").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/carts/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/carts/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/wishes/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/wishes/**").hasRole("USER")
                        .requestMatchers("/wishes/**").hasRole("USER")
                        .requestMatchers("/orders/**").hasRole("USER")
                        .requestMatchers("/reviews/**").hasRole("USER")
                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                );

        // 사용자 로그인 페이지
        http.formLogin(form -> form.disable());

        // 로그아웃 기능
        http.logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        // remember-me 설정
        http.rememberMe(rememberMe -> rememberMe
                .key("123456789")
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(60 * 60 * 24 * 365)
                .userDetailsService(userDetailsService)
                .authenticationSuccessHandler(new UserAuthSuccessHandler(jwtToken))
        );


        //exception Handler 설정
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new UserAccessDeniedHandler());
        });



        return http.build();
    }

    // 정적 자원들에 필터 적용 제외
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("----------web configure-----------");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);         // 통신을 위함
        return repo;
    }

}