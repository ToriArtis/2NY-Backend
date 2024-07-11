
package com.mega._NY.auth.service;
import com.mega._NY.auth.entity.OAuthAttributes;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserRoles;
import com.mega._NY.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Service {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final UserRepository userRepository;

    public OAuth2User processCode(String code, String registrationId) {
        log.info("Processing code for provider: {}", registrationId);
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration == null) {
            log.error("Client registration not found for provider: {}", registrationId);
            throw new IllegalArgumentException("Invalid provider");
        }

        try {
            OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                    .clientId(clientRegistration.getClientId())
                    .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                    .redirectUri(clientRegistration.getRedirectUri())
                    .scopes(clientRegistration.getScopes())
                    .state("state")
                    .build();

            OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse
                    .success(code)
                    .redirectUri(clientRegistration.getRedirectUri())
                    .state("state")
                    .build();

            OAuth2AuthorizationCodeGrantRequest grantRequest =
                    new OAuth2AuthorizationCodeGrantRequest(clientRegistration,
                            new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));

            DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
            OAuth2AccessTokenResponse tokenResponse = tokenResponseClient.getTokenResponse(grantRequest);

            DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
            OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, tokenResponse.getAccessToken());
            OAuth2User oAuth2User = userService.loadUser(userRequest);

            OAuthAttributes attributes = OAuthAttributes.of(registrationId, clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(), oAuth2User.getAttributes());
            User user = saveOrUpdate(attributes);

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(UserRoles.USER.name())), // 기본 역할 설정
                    attributes.getAttributes(),
                    attributes.getNameAttributeKey());
        } catch (Exception e) {
            log.error("Error processing OAuth2 code", e);
            throw e;
        }
    }

    public User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.updateUser(attributes.getName(), attributes.getEmail(), attributes.getProvider()))
                .orElseGet(() -> attributes.toEntity());

        if (user.getRoleSet().isEmpty()) {
            user.addRole(UserRoles.USER); // 기본 역할 추가
        }

        return userRepository.save(user);
    }
}