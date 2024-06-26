package com.mega._NY.repository;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.repository.CartRepository;
import com.mega._NY.cart.repository.ItemCartRepository;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.OrderStatus;
import com.mega._NY.orders.entity.Orders;
import com.mega._NY.orders.repository.OrdersRepository;
import com.mega._NY.orders.service.ItemOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
//@Transactional
public class OrderRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ItemCartRepository itemCartRepository;

    @Autowired
    private ItemOrdersService itemOrdersService;
    @Autowired
    private CartRepository cartRepository;

    @Test
    public void testCreateOrder() {
        // 사용자 조회
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자의 카트 조회
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            throw new RuntimeException("Cart not found for user");
        }

        // 카트에서 아이템 조회
        List<ItemCart> itemCarts = itemCartRepository.findAllByCart(cart);
        if (itemCarts.isEmpty()) {
            throw new RuntimeException("No items in cart");
        }

        // 주문 생성
        Orders order = new Orders();
        order.setUser(user);
        order.setName("주문자");
        order.setAddress("fff");
        order.setDetailAddress("dddd");
        order.setPhone("010-1234-5678");
        order.setOrderStatus(OrderStatus.ORDER_REQUEST);

        List<ItemOrders> itemOrders = new ArrayList<>();
        int totalPrice = 0;
        int totalItems = 0;
        int totalDiscountPrice = 0;

        for (ItemCart itemCart : itemCarts) {
            ItemOrders itemOrder = new ItemOrders();
            itemOrder.setItem(itemCart.getItem());
            itemOrder.setQuantity(itemCart.getQuantity());
            itemOrder.setOrders(order);
            itemOrders.add(itemOrder);

            totalPrice = itemOrdersService.calculateTotalPrice(itemOrders);
            totalItems += itemCart.getQuantity();
            totalDiscountPrice = itemOrdersService.calculateDiscountTotalPrice(itemOrders);
        }

        order.setItemOrders(itemOrders);
        order.setTotalPrice(totalPrice);
        order.setTotalItems(totalItems);
        order.setTotalDiscountPrice(totalDiscountPrice);
        order.setExpectPrice(totalPrice - totalDiscountPrice);

        // 주문 저장
        Orders savedOrder = ordersRepository.save(order);

        // 로그로 저장된 주문 정보 출력
        log.info("Saved Order ID: {}", savedOrder.getOrderId());
        log.info("Order Status: {}", savedOrder.getOrderStatus());
        log.info("Order Items Count: {}", savedOrder.getItemOrders().size());
        log.info("Total Price: {}", savedOrder.getTotalPrice());

        // 데이터베이스에서 주문 조회 및 검증
        Orders foundOrder = ordersRepository.findById(savedOrder.getOrderId())
                .orElseThrow(() -> new RuntimeException("Saved order not found"));

        log.info("Found Order ID: {}", foundOrder.getOrderId());
        log.info("Found Order Status: {}", foundOrder.getOrderStatus());
        log.info("Found Order Items Count: {}", foundOrder.getItemOrders().size());
        log.info("Found Total Price: {}", foundOrder.getTotalPrice());

        // 검증
        Assertions.assertNotNull(foundOrder);
        Assertions.assertEquals(OrderStatus.ORDER_REQUEST, foundOrder.getOrderStatus());
        Assertions.assertEquals(itemCarts.size(), foundOrder.getItemOrders().size());
        Assertions.assertEquals(totalPrice, foundOrder.getTotalPrice());
    }

}
