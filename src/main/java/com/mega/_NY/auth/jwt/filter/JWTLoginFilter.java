package com.mega._NY.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega._NY.auth.config.details.PrincipalDetails;
import com.mega._NY.auth.dto.LoginDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.jwt.JwtToken;
import com.mega._NY.auth.repository.UserRepository;
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
@Log4j2
public class JWTLoginFilter  extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    @Override
    @SneakyThrows
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException{
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDTO loginDto = objectMapper.readValue(request.getInputStream(), LoginDTO.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult ) throws IOException, ServletException{
        logger.info("successful Authentication");
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

}
