package com.mega._NY.orders.controller;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import com.mega._NY.orders.dto.ItemOrderDTO;
import com.mega._NY.orders.dto.OrdersDTO;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.Orders;
import com.mega._NY.orders.mapper.ItemOrdersMapper;
import com.mega._NY.orders.mapper.OrdersMapper;
import com.mega._NY.orders.service.OrdersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;
    private final ItemOrdersMapper itemOrdersMapper;
    private final OrdersMapper ordersMapper;
    private final ItemRepository itemRepository;

    // 새로운 주문 생성
    @PostMapping("/{userId}")
    public ResponseEntity<OrdersDTO> createOrder(@RequestBody @Valid List<ItemOrderDTO> itemOrderDtos,
                                                 @PathVariable("userId") @Positive Long userId) {
        List<ItemOrders> itemOrders = itemOrderDtos.stream()
                .map(this::convertToItemOrders)
                .collect(Collectors.toList());

        Orders order = ordersService.createOrder(itemOrders, userId);
        return ResponseEntity.ok(ordersMapper.orderToOrdersDTO(order));
    }

    // 장바구니에서 주문 생성
    @PostMapping("/from-cart/{userId}")
    public ResponseEntity<OrdersDTO> createOrderFromCart(@PathVariable("userId") @Positive Long userId) {
        Orders order = ordersService.createOrderFromCart(userId);
        return ResponseEntity.ok(ordersMapper.orderToOrdersDTO(order));
    }

    // 주문 목록 조회
    @GetMapping({"/list/{userId}"})
    public ResponseEntity<Page<OrdersDTO>> getOrders(@PathVariable("userId") @Positive Long userId,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Page<Orders> orders = ordersService.findOrders(userId, page, size);
        return ResponseEntity.ok(orders.map(ordersMapper::orderToOrdersDTO));
    }

    // 특정 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersDTO> getOrder(@PathVariable Long orderId) {
        Orders order = ordersService.findOrder(orderId);
        OrdersDTO dto = ordersMapper.orderToOrdersDTO(order);
        log.debug("Order DTO: {}", dto);
        return ResponseEntity.ok(dto);
    }

    // 주문 취소
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        ordersService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    // ItemOrderDTO를 ItemOrders 엔티티로 변환하는 헬퍼 메소드
    private ItemOrders convertToItemOrders(ItemOrderDTO dto) {
        ItemOrders io = itemOrdersMapper.itemOrderDtoToItemOrder(dto);
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ITEM_NOT_FOUND));
        io.setItem(item);
        io.setPrice(item.getPrice());
        io.setQuantity(dto.getQuantity());
        io.calculatePrices();
        return io;
    }

}
