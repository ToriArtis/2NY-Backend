package com.mega._NY.orders.mapper;

import com.mega._NY.orders.dto.OrdersDTO;
import com.mega._NY.orders.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemOrdersMapper.class})
public interface OrdersMapper {

    // 주문 엔티티를 DTO로 변환
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "itemOrders", target = "itemOrders")
    @Mapping(source = "user.realName", target = "name")
    OrdersDTO orderToOrdersDTO(Orders order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "itemOrders", ignore = true)
    Orders ordersDTOToOrder(OrdersDTO ordersDTO);

}
