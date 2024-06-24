package com.mega._NY.cart.entity;

import com.mega._NY.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ItemCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemCartId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean buyNow;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "CART_ID")
    private Cart cart;

    // 장바구니에 상품 추가 (양방향 관계)
    public void addCart(Cart cart) {
        this.cart = cart;
        if(!this.cart.getItemCarts().contains(this)) {
            this.cart.getItemCarts().add(this);
        }
    }

    // 장바구니에 같은 상품을 또 담을 경우 수량만 증가
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

}
