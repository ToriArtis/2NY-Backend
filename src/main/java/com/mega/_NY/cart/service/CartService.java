package com.mega._NY.cart.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemCartService itemCartService;
    private final UserService userService;

    public void refreshCart(long cartId) { // 가격과 아이템 종류 갱신
        Cart cart = findVerifiedCart(cartId);

        cart.setTotalPrice(countTotalPrice(cartId));
        cart.setTotalItems(countTotalItems(cartId));

        cartRepository.save(cart);
    }

    public Cart findMyCart() {
        User user = userService.getLoginUser();
        return cartRepository.findByUser(user);
    }

    public Cart findCart(long cartId) {
        Cart findCart = findVerifiedCart(cartId);
        return findCart;
    }

    public Cart findVerifiedCart(long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        Cart findCart = optionalCart.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.CART_NOT_FOUND));
        return findCart;
    }

    public int countTotalDiscountPrice(long cartId) {
        Cart cart = findVerifiedCart(cartId);
        List<ItemCart> itemCarts = itemCartService.findItemCarts(cart, true);

        if(itemCarts == null) return 0;

        int totalDiscountPrice = 0;

        for(ItemCart itemCart : itemCarts) {
            int quantity = itemCart.getItem().getPrice();
            int price = itemCart.getQuantity();
            int discountRate = itemCart.getItem().getDiscountRate();

            totalDiscountPrice += (quantity * price * discountRate/100);
        }

        return totalDiscountPrice;
    }

    private int countTotalPrice(long cartId) {
        Cart cart = findVerifiedCart(cartId);
        List<ItemCart> itemCarts = itemCartService.findItemCarts(cart, true);

        if(itemCarts == null) return 0;

        int totalPrice = 0;

        for(ItemCart itemCart : itemCarts) {
            int quantity = itemCart.getItem().getPrice();
            int price = itemCart.getQuantity();
            totalPrice += (quantity * price);
        }

        return totalPrice;
    }

    private int countTotalItems(long cartId) {
        Cart cart = findVerifiedCart(cartId);
        return itemCartService.findItemCarts(cart, true).size();
    }

}
