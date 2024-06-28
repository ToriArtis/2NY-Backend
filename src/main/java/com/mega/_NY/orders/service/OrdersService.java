package com.mega._NY.orders.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.service.CartService;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.OrderStatus;
import com.mega._NY.orders.entity.Orders;
import com.mega._NY.orders.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private final ItemRepository itemRepository;

    // 새로운 주문 생성
    public Orders createOrder(List<ItemOrders> itemOrders, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // Item 정보를 데이터베이스에서 불러옵니다.
        itemOrders.forEach(io -> {
            Item item = itemRepository.findById(io.getItem().getItemId())
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ITEM_NOT_FOUND));
            io.setItem(item);
        });

        Orders order = new Orders();
        setOrderInfo(order, user);
        order.setItemOrders(itemOrders);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.ORDER_REQUEST);
        order.setTotalPrice(itemOrdersService.calculateTotalPrice(itemOrders));
        order.setTotalDiscountPrice(itemOrdersService.calculateDiscountTotalPrice(itemOrders));
        order.setExpectPrice(order.getTotalPrice() - order.getTotalDiscountPrice());

        for (ItemOrders io : itemOrders) {
            io.setOrders(order);
            itemOrdersService.createItemOrder(io);
            itemOrdersService.updateItemSales(io, true);
        }

        return orderRepository.save(order);
    }

    public Orders createOrderFromCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for ID: {}", userId);
                    return new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
                });
        log.info("user : " + user);

        Cart cart = cartService.findVerifiedCart(user.getId());
        if (cart.getItemCarts().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.CART_NOT_FOUND);
        }

        Orders order = new Orders();
        setOrderInfo(order, user);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.ORDER_REQUEST);

        Orders savedorder = orderRepository.save(order);

        List<ItemOrders> itemOrders = cart.getItemCarts().stream()
                .map(itemCart ->
                        createItemOrderFromCartItem(itemCart, savedorder))
                .collect(Collectors.toList());

        order.setItemOrders(itemOrders);
        order.setTotalPrice(itemOrdersService.calculateTotalPrice(itemOrders));
        order.setTotalDiscountPrice(itemOrdersService.calculateDiscountTotalPrice(itemOrders));
        order.setExpectPrice(order.getTotalPrice() - order.getTotalDiscountPrice());

        order = orderRepository.save(order);

        itemOrders.forEach(io -> {
            itemOrdersService.updateItemSales(io, true);
        });

        // 주문 생성 후 카트 비우기
        cartService.clearCart(user);

        return order;
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
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
    }

    // 사용자의 주문 목록 조회 (페이지네이션)
    public Page<Orders> findOrders(Long userId, int page, int size) {
        return orderRepository.findAllByUserId(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderId")));
    }

    private void setOrderInfo(Orders order, User user) {
        order.setName(user.getName());
        order.setAddress("user.getAddress()");
        order.setDetailAddress("user.getDetailAddress()");
        order.setPhone("user.getPhone()");
    }

    private ItemOrders createItemOrderFromCartItem(ItemCart itemCart, Orders order) {
        ItemOrders itemOrder = new ItemOrders();
        itemOrder.setItem(itemCart.getItem());
        itemOrder.setQuantity(itemCart.getQuantity());
        itemOrder.setOrders(order);
        return itemOrdersService.createItemOrder(itemOrder);
    }

}
