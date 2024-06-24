package com.mega._NY.cart.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartDTO {

    @Positive
    private Long cartId;
    private int totalItems;
    private int totalPrice;
    private int totalDiscountPrice;
    private int expectPrice; // 결제 예상 금액 (totalPrice - totalDiscountPrice)

}
