package com.mega._NY.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private Long itemId;
    private String thumbnail;
    private String descriptionImage;
    private String title;
    private String content;
    private int sales;
    private int price;
    private int servingSize;
    private int discountRate;
    private int discountPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
