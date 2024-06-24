package com.mega._NY.orders.dto;

import com.mega._NY.item.dto.ItemDTO;
import jakarta.validation.constraints.Min;
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

}
