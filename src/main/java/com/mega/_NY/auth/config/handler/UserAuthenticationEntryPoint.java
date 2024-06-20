package com.mega._NY.auth.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException; // javax.naming 패키지가 아닌 org.springframework.security.core 패키지에 있습니다.
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("AuthenticationEntryPoint");
        log.info(authException.getMessage());
        errorToJson(response, authException, HttpStatus.UNAUTHORIZED);
    }

    public static void errorToJson(HttpServletResponse response, Exception exception, HttpStatus status) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        // ErrorResponse 대신 ResponseEntity 사용
        ResponseEntity<Object> responseEntity = ResponseEntity.status(status)
                .body(new HashMap() {{
                    put("status", status.value());
                    put("message", exception.getMessage());
                }});

        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponse = objectMapper.writeValueAsString(responseEntity.getBody());

        response.getWriter().write(errorResponse);
    }
}