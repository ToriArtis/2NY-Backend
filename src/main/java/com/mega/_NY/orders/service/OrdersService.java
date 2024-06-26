package com.mega._NY.orders.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.OrderStatus;
import com.mega._NY.orders.entity.Orders;
import com.mega._NY.orders.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository orderRepository;
    private final ItemOrdersService itemOrdersService;

    // 새로운 주문 생성
    public Orders createOrder(List<ItemOrders> itemOrders, User user) {
        Orders order = new Orders();
        order.setItemOrders(itemOrders);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.ORDER_REQUEST);
        order.setTotalPrice(itemOrdersService.calculateTotalPrice(itemOrders));

        itemOrders.forEach(io -> {
            io.setOrders(order);
            itemOrdersService.updateItemSales(io, true);
        });

        return orderRepository.save(order);
    }

    // 주문 취소
    public void cancelOrder(long orderId) {
        Orders order = findOrder(orderId);
        order.setOrderStatus(OrderStatus.ORDER_CANCEL);
        order.getItemOrders().forEach(io -> itemOrdersService.updateItemSales(io, false));
        orderRepository.save(order);
    }

    // ID로 주문 조회
    public Orders findOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
    }

    // 사용자의 주문 목록 조회 (페이지네이션)
    public Page<Orders> findOrders(Long userId, int page, int size) {
        return orderRepository.findAllByUserId(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderId")));
    }

}
