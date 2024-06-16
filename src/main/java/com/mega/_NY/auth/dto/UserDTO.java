package com.mega._NY.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class UserDTO {

    @Data
    public static class Post {  // post 할 때 받는 dto
        @NotBlank private String email; // 사용자 이메일
        @NotBlank private String password; // 사용자 비밀번호
        @NotBlank private String address; // 사용자 주소
        @NotBlank private String detailAddress; // 사용자 주소
        @NotBlank private String nickName; // 사용자 닉네임
        @NotBlank private String realName; // 사용자의 실제 이름
        @NotBlank private String phone; // 사용자 전화번호
    }

    @Data
    public static class PostMoreInfo {
        @NotNull private String email; // 사용자 이메일
        @NotNull private String address; // 사용자 주소
        @NotNull private String detailAddress; // 사용자 주소
        @NotNull private String nickName; // 사용자 닉네임
        @NotNull private String realName; // 사용자의 실제 이름
        @NotNull private String phone; // 사용자 전화번호
    }

    @Data
    public static class Get { // get 할 때 받는 dto
        private String email; // 사용자 이메일
        private String address; // 사용자 주소
        private String detailAddress; // 사용자 상세 주소
        private String nickName; // 사용자 닉네임
        private String realName; // 사용자의 실제 이름
        private String phone; // 사용자 전화번호
        private boolean social; // 소셜 로그인 여부
    }

    @Data
    @Builder
    public static class Response { // 응답 dto
        private String email; // 사용자 이메일
        private String address; // 사용자 주소
        private String detailAddress; // 사용자 상세 주소
        private String nickName; // 사용자 닉네임
        private String realName; // 사용자의 실제 이름
        private String phone; // 사용자 전화번호
        private String password; // 사용자 비밀번호 (보안상 응답에 포함하지 않는 것이 좋음)
        private boolean social; // 소셜 로그인 여부
        private LocalDateTime updatedAt; // 사용자 정보 마지막 업데이트 시간
    }

}
