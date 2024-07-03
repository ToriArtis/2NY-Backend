package com.mega._NY.cart.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCartDTO {

    private Long itemCartId;
    private Long itemId;
    private Long cartId;
    @Min(value = 1, message = "수량은 1개 이상 선택해주세요.")
    private int quantity;
    private int price;
    private int discountRate;

}
