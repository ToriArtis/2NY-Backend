package com.mega._NY.cart.entity;

import com.mega._NY.item.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
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

    @Column
    @ColumnDefault("0")
    private int period;

    @Column(nullable = false)
    private boolean buyNow;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Cart cart;

}
