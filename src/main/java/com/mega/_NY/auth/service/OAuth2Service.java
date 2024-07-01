package com.mega._NY.auth.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.OAuthAttributes;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("OAuth2UserService: loadUser method started");
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        User userProfile = OAuthAttributes.extract(registrationId, attributes);
        userProfile.setProvider(registrationId);

        try {
            User user = updateOrSaveUser(userProfile);
            Map<String, Object> customAttribute = getCustomAttribute(registrationId, userNameAttributeName, attributes, userProfile);

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    customAttribute,
                    userNameAttributeName);
        }  catch (BusinessLogicException e) {
            OAuth2Error oauth2Error = new OAuth2Error(e.getExceptionCode().toString(), e.getMessage(), null);
            throw new OAuth2AuthenticationException(oauth2Error, e.getMessage());
        }
    }

    public Map<String, Object> getCustomAttribute(String registrationId,
                                                  String userNameAttributeName,
                                                  Map<String, Object> attributes,
                                                  User user) {
        Map<String, Object> customAttribute = new ConcurrentHashMap<>();

        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", user.getRealName());
        customAttribute.put("email", user.getEmail());

        return customAttribute;
    }


    public User updateOrSaveUser(User user) {
        log.info("Updating or saving user: {}", user);
        return userRepository.findUserByEmailAndProvider(user.getEmail(), user.getProvider())
                .map(existingUser -> {
                    log.info("Existing user found: {}", existingUser);
                    return updateExistingUser(existingUser, user);
                })
                .orElseGet(() -> {
                    log.info("Creating new user");
                    return createNewUser(user);
                });
    }

    private User updateExistingUser(User existingUser, User userProfile) {
        log.info("Updating existing user: {} with new data: {}", existingUser, userProfile);
        existingUser.updateUser(userProfile.getRealName(), userProfile.getEmail());
        User savedUser = userRepository.save(existingUser);
        log.info("User updated and saved: {}", savedUser);
        return savedUser;
    }

    private User createNewUser(User user) {
        log.info("Attempting to create new user: {}", user);
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Email already exists: {}", user.getEmail());
            throw new BusinessLogicException(ExceptionCode.EXIST_EMAIL);
        }
        User savedUser = userRepository.save(user);
        log.info("New user created and saved: {}", savedUser);
        return savedUser;
    }



}