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
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemOrderId;

    @Column
    @ColumnDefault("0")
    private int quantity;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Orders orders;

}
