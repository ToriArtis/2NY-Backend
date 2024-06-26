package com.mega._NY.orders.dto;

import com.mega._NY.item.dto.ItemDTO;
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
    private int quantity; // 주문 수량
    private ItemDTO item;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
