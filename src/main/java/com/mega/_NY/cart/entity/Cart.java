package com.mega._NY.cart.entity;

import com.mega._NY.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(name = "USER_ID", unique = true)
    private Long userId;

    @Column
    @Setter
    private int totalItems;

    @Column
    @Setter
    private int totalPrice;

    @Column
    @Setter
    private int totalDiscountPrice;

    // User에 대한 단방향 일대일 관계 (옵션)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // setUser 메서드 수정
    public void setUser(User user) {
        if (user != null) {
            this.userId = user.getId();
            this.user = user;
        }
    }

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    @Builder.Default
    List<ItemCart> itemCarts = new ArrayList<>();

    // 장바구니에 상품 추가
    public void addItemCart(ItemCart itemCart) {
        this.itemCarts.add(itemCart);
        if(itemCart.getCart() != this) {
            itemCart.addCart(this);
        }
    }

    // 새 장바구니 생성 (사용자당 하나의 장바구니)
    public static Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cart;
    }

}
