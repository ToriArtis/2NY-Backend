package com.mega._NY.auth.dto;

import com.mega._NY.auth.entity.UserRoles;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class RoleModifyRequestDTO {
        private String password;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class ResponseDTO {

        @NotBlank private String email; // 사용자 이메일
        @NotBlank private String password; // 사용자 비밀번호
        @NotBlank private String address; // 사용자 주소
        @NotBlank private String detailAddress; // 사용자 주소
        @NotBlank private String nickName; // 사용자 닉네임
        @NotBlank private String realName; // 사용자의 실제 이름
        @NotBlank private String phone; // 사용자 전화번호
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class LoginDTO{
        private String accessToken;
        private String refreshToken;

        @NotBlank private String email; // 사용자 이메일
        @NotBlank private String nickName; // 사용자 닉네임
        @NotBlank private Set<UserRoles> roleSet;
        private String provider;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindDTO{
        private String phone;
    }
}
