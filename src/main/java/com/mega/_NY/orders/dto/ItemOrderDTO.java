package com.mega._NY.orders.dto;

import com.mega._NY.item.entity.ItemColor;
import com.mega._NY.item.entity.ItemSize;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String itemTitle;  // Item의 이름
    @Min(value = 1, message = "수량은 1개 이상 선택해주세요.")
    private int quantity;     // 주문 수량
    private int price;        // 개별 아이템 가격
    private int totalPrice;   // 수량 * 가격

    private ItemSize size;
    private ItemColor color;


}
