package com.mega._NY.orders.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderDTO {

    private Long itemOrderId;
    private Long itemId;
    private String itemTitle;  // Item의 이름
    private int quantity;     // 주문 수량
    private int price;        // 개별 아이템 가격
    private int totalPrice;   // 수량 * 가격

}
