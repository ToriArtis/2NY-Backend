package com.mega._NY.auth.config;

import com.mega._NY.auth.dto.UserDTO;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserStatus;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class UserMapperConfig {

    String DEFAULT_OAUTH2_ID = "default";

    @Bean
    public UserMapperConfig userMapper() {
        return new UserMapperConfig() {

        };
    }
    public UserDTO.Response userToDto(User user, HttpMethod method) {
        return UserDTO.Response.builder()
                .address(user.getAddress())
                .phone(user.getPhone())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .realName(user.getRealName())
                .updatedAt(method == HttpMethod.GET ? user.getCreatedAt() : LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .password("정보가 변경되었습니다.")
                .social(!user.getProviderId().equals("default"))
                .build();
    }

    public User dtoToUser(UserDTO.Post userDto) {
        User.UserBuilder userBuilder = User.builder();
        userBuilder
                .email(userDto.getEmail())
                .nickName(userDto.getNickName())
                .password(userDto.getPassword())
                .address(userDto.getAddress())
                .detailAddress(userDto.getDetailAddress())
                .realName(userDto.getRealName())
                .phone(userDto.getPhone())
                .oAuthId(DEFAULT_OAUTH2_ID)
                .userStatus(UserStatus.USER_ACTIVE)
                .provider(DEFAULT_OAUTH2_ID)
                .providerId(DEFAULT_OAUTH2_ID);
        return userBuilder.build();
    }
}
