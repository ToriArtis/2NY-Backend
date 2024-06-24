package com.mega._NY.orders.mapper;

import com.mega._NY.orders.dto.OrdersDTO;
import com.mega._NY.orders.entity.Orders;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

    // 주문 엔티티를 DTO로 변환
    OrdersDTO orderToOrdersDTO(Orders order);

}
