package com.mega._NY.orders.controller;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.orders.dto.ItemOrderDTO;
import com.mega._NY.orders.dto.OrdersDTO;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.Orders;
import com.mega._NY.orders.mapper.ItemOrdersMapper;
import com.mega._NY.orders.mapper.OrdersMapper;
import com.mega._NY.orders.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;
    private final UserService userService;
    private final ItemOrdersMapper itemOrdersMapper;
    private final OrdersMapper ordersMapper;

    // 새로운 주문 생성
    @PostMapping
    public ResponseEntity<OrdersDTO> createOrder(@RequestBody @Valid List<ItemOrderDTO> itemOrderDtos) {
        User user = userService.getLoginUser();
        List<ItemOrders> itemOrders = itemOrdersMapper.itemOrderDtosToItemOrders(itemOrderDtos);
        Orders order = ordersService.createOrder(itemOrders, user);
        return ResponseEntity.ok(ordersMapper.orderToOrdersDTO(order));
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<Page<OrdersDTO>> getOrders(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        User user = userService.getLoginUser();
        Page<Orders> orders = ordersService.findOrders(user, page, size);
        return ResponseEntity.ok(orders.map(ordersMapper::orderToOrdersDTO));
    }

    // 특정 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersDTO> getOrder(@PathVariable Long orderId) {
        Orders order = ordersService.findOrder(orderId);
        return ResponseEntity.ok(ordersMapper.orderToOrdersDTO(order));
    }

    // 주문 취소
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        ordersService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }


}
