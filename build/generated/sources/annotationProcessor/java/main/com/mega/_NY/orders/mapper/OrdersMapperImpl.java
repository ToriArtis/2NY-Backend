package com.mega._NY.orders.mapper;

import com.mega._NY.auth.entity.User;
import com.mega._NY.orders.dto.OrdersDTO;
import com.mega._NY.orders.dto.OrdersDTO.OrdersDTOBuilder;
import com.mega._NY.orders.entity.OrderStatus;
import com.mega._NY.orders.entity.Orders;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-27T14:13:05+0900",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class OrdersMapperImpl implements OrdersMapper {

    @Autowired
    private ItemOrdersMapper itemOrdersMapper;

    @Override
    public OrdersDTO orderToOrdersDTO(Orders order) {
        if ( order == null ) {
            return null;
        }

        OrdersDTOBuilder ordersDTO = OrdersDTO.builder();

        ordersDTO.userId( orderUserId( order ) );
        ordersDTO.itemOrders( itemOrdersMapper.itemOrdersToItemOrderDtos( order.getItemOrders() ) );
        ordersDTO.name( orderUserRealName( order ) );
        ordersDTO.email( orderUserEmail( order ) );
        ordersDTO.orderId( order.getOrderId() );
        ordersDTO.address( order.getAddress() );
        ordersDTO.detailAddress( order.getDetailAddress() );
        ordersDTO.phone( order.getPhone() );
        if ( order.getOrderStatus() != null ) {
            ordersDTO.orderStatus( order.getOrderStatus().name() );
        }
        ordersDTO.totalItems( order.getTotalItems() );
        ordersDTO.totalPrice( order.getTotalPrice() );
        ordersDTO.totalDiscountPrice( order.getTotalDiscountPrice() );
        ordersDTO.expectPrice( order.getExpectPrice() );
        ordersDTO.createdAt( order.getCreatedAt() );
        ordersDTO.updatedAt( order.getUpdatedAt() );

        return ordersDTO.build();
    }

    @Override
    public Orders ordersDTOToOrder(OrdersDTO ordersDTO) {
        if ( ordersDTO == null ) {
            return null;
        }

        Orders orders = new Orders();

        orders.setOrderId( ordersDTO.getOrderId() );
        orders.setName( ordersDTO.getName() );
        orders.setAddress( ordersDTO.getAddress() );
        orders.setDetailAddress( ordersDTO.getDetailAddress() );
        orders.setPhone( ordersDTO.getPhone() );
        if ( ordersDTO.getOrderStatus() != null ) {
            orders.setOrderStatus( Enum.valueOf( OrderStatus.class, ordersDTO.getOrderStatus() ) );
        }
        orders.setUserId( ordersDTO.getUserId() );
        orders.setCreatedAt( ordersDTO.getCreatedAt() );
        orders.setUpdatedAt( ordersDTO.getUpdatedAt() );
        orders.setTotalItems( ordersDTO.getTotalItems() );
        orders.setTotalPrice( ordersDTO.getTotalPrice() );
        orders.setTotalDiscountPrice( ordersDTO.getTotalDiscountPrice() );
        orders.setExpectPrice( ordersDTO.getExpectPrice() );

        return orders;
    }

    private Long orderUserId(Orders orders) {
        if ( orders == null ) {
            return null;
        }
        User user = orders.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String orderUserRealName(Orders orders) {
        if ( orders == null ) {
            return null;
        }
        User user = orders.getUser();
        if ( user == null ) {
            return null;
        }
        String realName = user.getRealName();
        if ( realName == null ) {
            return null;
        }
        return realName;
    }

    private String orderUserEmail(Orders orders) {
        if ( orders == null ) {
            return null;
        }
        User user = orders.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }
}
