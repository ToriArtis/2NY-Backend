package com.mega._NY.auth.entity;


import com.mega._NY.cart.entity.Cart;
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
public class User  implements Principal{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "email")
    private String email;

    @Column(name = "nick_name", length = 20)
    private String nickName;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private UserStatus userStatus = UserStatus.USER_ACTIVE; // 기본값을 USER_ACTIVE로 설정합니다.

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public String getName() {
        return getEmail();
    }

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "CART_ID")
    private Cart cart;

    public void setCart(Cart cart) {
        this.cart = cart;
        if (cart != null && cart.getUser() != this) {
            cart.setUser(this);
        }
    }

}