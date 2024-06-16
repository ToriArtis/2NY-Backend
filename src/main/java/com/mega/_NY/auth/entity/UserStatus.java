package com.mega._NY.auth.entity;


import lombok.Getter;

public enum UserStatus {

    USER_ACTIVE("활동 중"),       // 사용자 상태: 활동 중
    USER_WITHDRAWAL("회원 탈퇴");  // 사용자 상태: 회원 탈퇴

    @Getter
    private String status;

    UserStatus( String status ){
        this.status = status;
    }
}
