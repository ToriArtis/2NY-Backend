package com.mega._NY.orders.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.service.CartService;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository orderRepository;
    private final ItemOrdersService itemOrdersService;
    private final CartService cartService;

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

    public Orders createOrderFromCart(User user) {
        Cart cart = cartService.findVerifiedCart(user.getId());
        if (cart.getItemCarts().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.CART_NOT_FOUND);
        }

        List<ItemOrders> itemOrders = cart.getItemCarts().stream()
                .map(cartItem -> {
                    ItemOrders itemOrder = new ItemOrders();
                    itemOrder.setItem(cartItem.getItem());
                    itemOrder.setQuantity(cartItem.getQuantity());
                    return itemOrder;
                })
                .collect(Collectors.toList());

        Orders order = new Orders();
        order.setItemOrders(itemOrders);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.ORDER_REQUEST);
        order.setTotalPrice(itemOrdersService.calculateTotalPrice(itemOrders));
        order.setTotalDiscountPrice(itemOrdersService.calculateDiscountTotalPrice(itemOrders));

        int totalPrice = itemOrdersService.calculateTotalPrice(itemOrders);
        int totalDiscountPrice = itemOrdersService.calculateDiscountTotalPrice(itemOrders);
        order.setExpectPrice(totalPrice - totalDiscountPrice);

        itemOrders.forEach(io -> {
            io.setOrders(order);
            itemOrdersService.createItemOrder(io);
            itemOrdersService.updateItemSales(io, true);
        });

        // 주문 생성 후 카트 비우기
        cartService.clearCart(user);

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
