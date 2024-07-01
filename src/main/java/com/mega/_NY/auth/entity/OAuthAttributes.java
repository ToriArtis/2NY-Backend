package com.mega._NY.auth.entity;

import com.mega._NY.auth.dto.UserDTO;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    GOOGLE("google", (attribute) -> {

        User user = new User();
        user.setRealName((String)attribute.get("name"));
        user.setEmail((String)attribute.get("email"));
        return user;
    });

//    NAVER("naver", (attribute) -> {
//        UserProfile userProfile = new UserProfile();
//        Map<String, String> responseValue = (Map)attribute.get("response");
//        userProfile.setUserName(responseValue.get("name"));
//        userProfile.setEmail(responseValue.get("email"));
//        return userProfile;
//    }),
//
//    KAKAO("kakao", (attribute) -> {
//        Map<String, Object> account = (Map)attribute.get("kakao_account");
//        Map<String, String> profile = (Map)account.get("profile");
//        UserProfile userProfile = new UserProfile();
//        userProfile.setUserName(profile.get("nickname"));
//        userProfile.setEmail((String)account.get("email"));
//        return userProfile;
//    });

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, User> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, User> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static User extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}