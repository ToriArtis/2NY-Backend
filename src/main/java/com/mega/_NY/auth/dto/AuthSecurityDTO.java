package com.mega._NY.auth.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Map;

public class AuthSecurityDTO extends User{


    @NotBlank
    private String email; // 사용자 이메일
    @NotBlank private String password; // 사용자 비밀번호
    @NotBlank private String address; // 사용자 주소
    @NotBlank private String detailAddress; // 사용자 주소
    @NotBlank private String nickName; // 사용자 닉네임
    @NotBlank private String realName; // 사용자의 실제 이름
    @NotBlank private String phone; // 사용자 전화번호


    private Map<String, Object> props;
    public AuthSecurityDTO(Long id, String email, String password,
                           String address, String detailAddress, String nickName,
                           String realName, String phone, Map<String, Object> props) {
        super(email, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.email = email;
        this.password = password;
        this.address = address;
        this.detailAddress = detailAddress;
        this.nickName = nickName;
        this.realName = realName;
        this.phone = phone;
        this.props = props;


    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
