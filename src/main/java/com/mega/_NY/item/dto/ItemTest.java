package com.mega._NY.item.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemTest {
    private String title;
    private String content;
    private int price;


    /*
    *     private String title;
    private String content;
    private int sales;
    private int price;
    private ItemSize size;
    private ItemColor color;
    private ItemCategory category;
    private int discountRate;
    private int discountPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private double avgStar;
    *
    * */
}
