package com.mega._NY.orders.dto;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.orders.entity.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDTO {

    private Long orderId;
    private String name;
    private String address;
    private String phone;
    private OrderStatus orderStatus;
    private List<ItemOrderDTO> itemOrders; // 주문 상품 목록
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
