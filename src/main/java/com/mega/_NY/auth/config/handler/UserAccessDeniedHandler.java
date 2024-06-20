package com.mega._NY.auth.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j
@Component
public class UserAccessDeniedHandler implements  AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("권한 없는 사용자");
        UserAuthenticationEntryPoint.errorToJson(response, accessDeniedException, HttpStatus.FORBIDDEN);
    }
}
