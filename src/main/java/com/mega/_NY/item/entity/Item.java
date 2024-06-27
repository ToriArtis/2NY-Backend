package com.mega._NY.item.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String thumbnail;

    @Column
    private String descriptionImage;

    @Column
    private int price;

    @Column
    private int discountPrice;

    @Column
    private int discountRate;

    @Column
    private int sales;

    @Enumerated(EnumType.STRING)
    @Column
    private ItemSize size;

    @Enumerated(EnumType.STRING)
    @Column
    private ItemColor color;

    @Enumerated(EnumType.STRING)
    @Column
    private ItemCategory category;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Builder
    public Item(String title, String content, String thumbnail, String descriptionImage,
                int price, int discountPrice, int discountRate, int sales, ItemSize size, ItemColor color) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.descriptionImage = descriptionImage;
        this.price = price;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
        this.sales = sales;
        this.size = size;
        this.color = color;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}