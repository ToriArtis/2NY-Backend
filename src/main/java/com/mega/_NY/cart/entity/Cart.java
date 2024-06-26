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

    @Column
    @Setter
    private int totalItems;

    @Column
    @Setter
    private int totalPrice;

    @Column
    @Setter
    private int totalDiscountPrice;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    @Builder.Default
    List<ItemCart> itemCarts = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getCart() != this) {
            user.setCart(this);
        }
    }

    // 장바구니에 상품 추가
    public void addItemCart(ItemCart itemCart) {
        this.itemCarts.add(itemCart);
        if(itemCart.getCart() != this) {
            itemCart.addCart(this);
        }
    }

    // 새 장바구니 생성 (사용자당 하나의 장바구니)
    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);  // 이 메서드 내에서 user.setCart(cart)도 호출됩니다.
        return cart;
    }

}
