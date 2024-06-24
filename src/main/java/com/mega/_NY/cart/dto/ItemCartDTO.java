package com.mega._NY.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCartDTO {

    private Long itemCartId;
    private Long itemId;
    private Long cartId;
    private int quantity;
    private int price;
    private int discountRate;

}
