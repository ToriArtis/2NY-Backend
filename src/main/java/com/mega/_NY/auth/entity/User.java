package com.mega._NY.auth.entity;


import jakarta.persistence.*;
import lombok.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "USERS")
public class User implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;       // 사용자 이메일 주소

    private String password;    // 사용자 비밀번호

    @Column(name = "REAL_NAME")
    private String realName;    // 사용자의 실제 이름

    private String nickName; // 사용자 표시 이름 (닉네임)

    private String address;     // 사용자 주소

    private String detailAddress;     // 사용자 주소

    @Column(unique = true)
    private String phone;       // 사용자 전화번호

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String provider;    // OAuth 제공자 (예: Google, Facebook)

    // 사용자 역할 목록을 저장합니다. 즉, 사용자가 가진 권한(역할)들을 저장합니다.
    // @ElementCollection: 기본 값 타입의 컬렉션을 매핑하기 위해 사용됩니다.
    // fetch = FetchType.EAGER: 즉시 로딩 전략을 사용하여 엔티티를 로드할 때 함께 로드합니다.
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    // 사용자 상태를 저장합니다. UserStatus 열거형을 사용합니다.
    // @Enumerated: 열거형 필드를 매핑하기 위해 사용됩니다.
    // EnumType.STRING: 열거형의 이름을 데이터베이스에 저장합니다.
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus = UserStatus.USER_ACTIVE; // 기본값을 USER_ACTIVE로 설정합니다.

    @Column(name = "PROVIDER_ID")
    private String providerId;  // OAuth 제공자에서의 사용자 ID

    @Column(name = "OAUTH_ID")
    private String oAuthId;   // 인증 ID (내부 시스템에서 사용)

    // 추후 추가 되는 사항들이기 때문에 코드 상에 반영은 하나 주석처리 함

//    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private Cart cart;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // user가 삭제될 경우 연관관계 wish 도 같이 삭제되도록 설정.
//    private List<Wish> wishList = new ArrayList<>();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Order> orders = new ArrayList<>();

    @Override
    public String getName(){
        return getEmail();
    }


}