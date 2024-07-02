package com.mega._NY.orders.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.service.CartService;
import com.mega._NY.cart.service.ItemCartService;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.OrderStatus;
import com.mega._NY.orders.entity.Orders;
import com.mega._NY.orders.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class OrdersService {

    private final OrdersRepository orderRepository;
    private final ItemOrdersService itemOrdersService;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final ItemCartService itemCartService;

    // 새로운 주문 생성
    public Orders createOrder(List<ItemOrders> itemOrders, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Orders order = new Orders();
        setOrderInfo(order, user);
        order.setOrderStatus(OrderStatus.ORDER_REQUEST);

        // 주문 항목 처리 및 총계 계산
        processOrderItems(order, itemOrders);

        return orderRepository.save(order);
    }

    // 장바구니에서 주문 생성
    public Orders createOrderFromCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Cart cart = cartService.findMyCart(userId);
        if (cart.getItemCarts().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.CART_NOT_FOUND);
        }

        Orders order = new Orders();
        setOrderInfo(order, user);
        order.setOrderStatus(OrderStatus.ORDER_REQUEST);
        final Orders savedOrder = orderRepository.save(order);  // final로 선언

        // 장바구니 항목을 주문 항목으로 변환
        List<ItemOrders> itemOrders = cart.getItemCarts().stream()
                .map(itemCart -> createItemOrderFromCartItem(itemCart, savedOrder))  // savedOrder 사용
                .collect(Collectors.toList());

        // 주문 항목 처리 및 총계 계산
        processOrderItems(savedOrder, itemOrders);

        cartService.clearCart(user);

        return orderRepository.save(savedOrder);
    }

    // 주문 취소
    public void cancelOrder(Long orderId) {
        Orders order = findOrder(orderId);
        order.setOrderStatus(OrderStatus.ORDER_CANCEL);
        order.getItemOrders().forEach(io -> itemOrdersService.updateItemSales(io, false));
        orderRepository.save(order);
    }

    // ID로 주문 조회
    public Orders findOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        order.getItemOrders().forEach(io -> Hibernate.initialize(io.getItem()));
        return order;
    }

    // 사용자의 주문 목록 조회 (페이지네이션)
    public Page<Orders> findOrders(Long userId, int page, int size) {
        return orderRepository.findAllByUserId(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderId")));
    }

    // 주문 정보 설정 헬퍼 메소드
    private void setOrderInfo(Orders order, User user) {
        order.setName(user.getName());
        order.setAddress(user.getAddress());
        order.setDetailAddress(user.getDetailAddress());
        order.setPhone(user.getPhone());
        order.setUserId(user.getId());
        order.setUser(user);
    }

    // 주문 항목 처리 및 총계 계산 헬퍼 메소드
    private void processOrderItems(Orders order, List<ItemOrders> itemOrders) {
        for (ItemOrders io : itemOrders) {
            io.setOrders(order);
            itemOrdersService.createItemOrder(io);
            itemOrdersService.updateItemSales(io, true);
        }

        order.setItemOrders(itemOrders);
        order.setTotalPrice(itemOrdersService.calculateTotalPrice(itemOrders));
        order.setTotalDiscountPrice(itemOrdersService.calculateDiscountTotalPrice(itemOrders));
        order.setExpectPrice(order.getTotalPrice() - order.getTotalDiscountPrice());
        order.setTotalItems(itemOrders.size());
    }

    // 장바구니 항목을 주문 항목으로 변환하는 헬퍼 메소드
    private ItemOrders createItemOrderFromCartItem(ItemCart itemCart, Orders order) {
        ItemOrders itemOrder = new ItemOrders();
        itemOrder.setItem(itemCart.getItem());
        itemOrder.setQuantity(itemCart.getQuantity());
        itemOrder.setOrders(order);
        return itemOrdersService.createItemOrder(itemOrder);
    }

    public Orders completeOrder(Long orderId) {
        Orders order = findOrder(orderId);
        order.setOrderStatus(OrderStatus.ORDER_COMPLETE);
        Orders completedOrder = orderRepository.save(order);

        // buyNow 상태 업데이트
        completedOrder.getItemOrders().forEach(io -> {
            Cart cart = cartService.findMyCart(order.getUserId());
            cart.getItemCarts().stream()
                    .filter(itemCart -> itemCart.getItem().getItemId().equals(io.getItem().getItemId()))
                    .findFirst()
                    .ifPresent(itemCart -> itemCartService.excludeItemCart(itemCart.getItemCartId(), true));
        });

        return completedOrder;
    }

}
