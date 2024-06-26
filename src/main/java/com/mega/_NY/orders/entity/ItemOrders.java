package com.mega._NY.orders.entity;

import com.mega._NY.item.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity(name = "ITEM_ORDERS")
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemOrderId;

    // 주문 수량
    @Column
    @ColumnDefault("0")
    private int quantity;

    // 주문한 상품
    @ManyToOne
    @JoinColumn(name="ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Orders orders;

    

}
