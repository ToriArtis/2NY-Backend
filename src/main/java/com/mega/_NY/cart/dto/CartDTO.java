package com.mega._NY.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private Long cartId;
    private Long userId;
    private int totalItems;
    private int totalPrice;
    private int totalDiscountPrice;
    private List<ItemCartDTO> itemCarts;

}
