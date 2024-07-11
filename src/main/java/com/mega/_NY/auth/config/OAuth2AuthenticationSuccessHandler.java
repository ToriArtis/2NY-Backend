package com.mega._NY.auth.config;

import com.mega._NY.auth.entity.UserRoles;
import com.mega._NY.auth.entity.UserStatus;
import com.mega._NY.auth.jwt.TokenProvider;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    existingUser.setRealName(name);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .realName(name)
                            .nickName(name)  // 임시로 realName을 nickName으로 사용
                            .provider("oauth2")
                            .userStatus(UserStatus.USER_ACTIVE)
                            .build();
                    return userRepository.save(newUser);
                });

        String token = tokenProvider.create(user);

        getRedirectStrategy().sendRedirect(request, response, "/oauth2/redirect?token=" + token);
    }
}