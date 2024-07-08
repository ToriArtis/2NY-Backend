package com.mega._NY.orders.controller;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserRoles;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import com.mega._NY.orders.dto.ItemOrderDTO;
import com.mega._NY.orders.dto.OrdersDTO;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.OrderStatus;
import com.mega._NY.orders.entity.Orders;
import com.mega._NY.orders.mapper.ItemOrdersMapper;
import com.mega._NY.orders.mapper.OrdersMapper;
import com.mega._NY.orders.repository.OrdersRepository;
import com.mega._NY.orders.service.OrdersService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    private final UserService userService;
    private final OrdersRepository ordersRepository;

    private boolean isUser() {
        User loginUser = userService.getLoginUser();
        return loginUser.getRoleSet().contains(UserRoles.USER);
    }

    private boolean isAdmin() {
        User loginUser = userService.getLoginUser();
        return loginUser.getRoleSet().contains(UserRoles.ADMIN);
    }

    // 새로운 주문 생성
    @PostMapping
    public ResponseEntity<OrdersDTO> createOrder(@RequestBody @Valid List<ItemOrderDTO> itemOrderDtos) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long userId = userService.getLoginUser().getId();

        List<ItemOrders> itemOrders = itemOrderDtos.stream()
                .map(this::convertToItemOrders)
                .collect(Collectors.toList());

        Orders order = ordersService.createOrder(itemOrders, userId);
        return ResponseEntity.ok(ordersMapper.orderToOrdersDTO(order));
    }

    // 장바구니에서 주문 생성
    @PostMapping("/from-cart")
    public ResponseEntity<OrdersDTO> createOrderFromCart() {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long userId = userService.getLoginUser().getId();
        Orders order = ordersService.createOrderFromCart(userId);
        return ResponseEntity.ok(ordersMapper.orderToOrdersDTO(order));
    }

    // 주문 목록 조회
    @GetMapping({"/list"})
    public ResponseEntity<Page<OrdersDTO>> getOrders(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long userId = userService.getLoginUser().getId();
        Page<Orders> orders = ordersService.findOrders(userId, page, size);
        return ResponseEntity.ok(orders.map(ordersMapper::orderToOrdersDTO));
    }

    // 모든 주문 목록 조회
    @GetMapping({"/all"})
    public ResponseEntity<Page<OrdersDTO>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Page<Orders> orders = ordersService.findAllOrders(page, size);
        return ResponseEntity.ok(orders.map(ordersMapper::orderToOrdersDTO));
    }

    // 특정 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersDTO> getOrder(@PathVariable Long orderId) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Orders order = ordersService.findOrder(orderId);
        OrdersDTO dto = ordersMapper.orderToOrdersDTO(order);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        User loginUser = userService.getLoginUser();
        if (!loginUser.getRoleSet().contains(UserRoles.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 필요합니다.");
        }
        try {
            Orders order = ordersService.findOrder(orderId);
            if (order.getOrderStatus() == OrderStatus.ORDER_CANCEL) {
                return ResponseEntity.badRequest().body("취소된 주문의 상태는 변경할 수 없습니다.");
            }
            Orders updatedOrder = ordersService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(ordersMapper.orderToOrdersDTO(updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 상태 변경 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 주문 취소
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

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
