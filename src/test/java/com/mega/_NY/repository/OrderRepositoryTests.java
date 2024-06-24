package com.mega._NY.repository;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.OrderStatus;
import com.mega._NY.orders.entity.Orders;
import com.mega._NY.orders.repository.ItemOrdersRepository;
import com.mega._NY.orders.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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
    private ItemRepository itemRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ItemOrdersRepository itemOrdersRepository;

    @Test
    public void testCreateOrder() {
        // 사용자 생성 및 저장
        User user = new User();
        user.setEmail("test@example.com");
        user.setRealName("Test User");
        User savedUser = userRepository.save(user);

        // 상품 생성 및 저장
        Item item = new Item();
        item.setTitle("Test Item");
        item.setPrice(1000);
        Item savedItem = itemRepository.save(item);

        // 주문 생성
        Orders order = new Orders();
        order.setUser(savedUser);
        order.setName(savedUser.getName());
        order.setAddress("Test Address");
        order.setPhone("010-1234-5678");
        order.setOrderStatus(OrderStatus.ORDER_REQUEST);

        // 주문 상품 생성
        ItemOrders itemOrder = new ItemOrders();
        itemOrder.setItem(savedItem);
        itemOrder.setQuantity(2);
        itemOrder.setOrders(order);

        List<ItemOrders> itemOrders = new ArrayList<>();
        itemOrders.add(itemOrder);
        order.setItemOrders(itemOrders);

        // 주문 저장
        Orders savedOrder = ordersRepository.save(order);

        // 로그로 저장된 주문 정보 출력
        log.info("Saved Order ID: {}", savedOrder.getOrderId());
        log.info("Order Status: {}", savedOrder.getOrderStatus());
        log.info("Order Items Count: {}", savedOrder.getItemOrders().size());

        // 데이터베이스에서 주문 조회
        Orders foundOrder = ordersRepository.findById(savedOrder.getOrderId()).orElse(null);
        if (foundOrder != null) {
            log.info("Found Order ID: {}", foundOrder.getOrderId());
            log.info("Found Order Status: {}", foundOrder.getOrderStatus());
            log.info("Found Order Items Count: {}", foundOrder.getItemOrders().size());
        } else {
            log.error("Order not found in database");
        }
    }

}
