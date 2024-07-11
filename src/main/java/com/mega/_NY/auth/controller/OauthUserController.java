package com.mega._NY.auth.controller;

import com.mega._NY.auth.entity.OAuthAttributes;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.jwt.TokenProvider;
import com.mega._NY.auth.service.OAuth2Service;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/oauth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OauthUserController {

    private final OAuth2Service oAuth2Service;
    private final TokenProvider tokenProvider;

    @Autowired
    public OauthUserController(OAuth2Service oAuth2Service, TokenProvider tokenProvider) {
        this.oAuth2Service = oAuth2Service;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody TokenRequest tokenRequest) {
        try {
            log.info("Received token request for provider: {}", tokenRequest.getProvider());
            OAuth2User oAuth2User = oAuth2Service.processCode(tokenRequest.getCode(), tokenRequest.getProvider());
            OAuthAttributes attributes = OAuthAttributes.of(tokenRequest.getProvider(), "sub", oAuth2User.getAttributes());
            User user = oAuth2Service.saveOrUpdate(attributes);
            String token = tokenProvider.create(user);
            log.info("Token created successfully for user: {}", user.getEmail());
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (Exception e) {
            log.error("Token exchange failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Token exchange failed: " + e.getMessage());
        }
    }

    @GetMapping("/loginInfo")
    public ResponseEntity<?> getJson(Authentication authentication) {
        log.info("controller commin");
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                Map<String, Object> attributes = oAuth2User.getAttributes();
                return ResponseEntity.ok(attributes);
            } else {
                return ResponseEntity.ok("User is authenticated but not an OAuth2User");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
    // TokenRequest와 TokenResponse 클래스 정의
    private static class TokenRequest {
        private String code;
        private String provider;
        // getters and setters
        public String getCode() {
            return code;
        }
        public String getProvider() {
            return provider;
        }
    }

    private static class TokenResponse {
        private String token;
        // constructor, getters and setters

        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public TokenResponse(String token) {
            this.token = token;
        }
    }
}



