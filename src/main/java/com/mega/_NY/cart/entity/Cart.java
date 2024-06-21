package com.mega._NY.cart.entity;

import com.mega._NY.auth.entity.User;
import com.mega._NY.item.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private int totalItems;

    @Column
    private int totalPrice;

    @Column
    private int totalDiscountPrice;

    @OneToOne
    private User user;

}
