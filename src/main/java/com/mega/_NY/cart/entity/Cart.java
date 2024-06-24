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
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    @Builder.Default
    List<ItemCart> itemCarts = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
    }

    public void addItemCart(ItemCart itemCart) {
        this.itemCarts.add(itemCart);
        if(itemCart.getCart() != this) {
            itemCart.addCart(this);
        }
    }

    // 회원 한 명이 하나의 장바구니를 가지므로 회원당 1회만 장바구니 생성
    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.user = user;
        user.setCart(cart);
        return cart;
    }

}
