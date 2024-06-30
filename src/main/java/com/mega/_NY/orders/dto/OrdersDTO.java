package com.mega._NY.orders.dto;

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
    private Long userId;
    private String name;
    private String address;
    private String detailAddress;
    private String phone;
    private OrderStatus orderStatus;
    private Integer totalItems;
    private Integer totalPrice;
    private Integer totalDiscountPrice;
    private Integer expectPrice;
    private List<ItemOrderDTO> itemOrders; // 주문 상품 목록
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
