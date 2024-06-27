package com.mega._NY.cart.service;

import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.mapper.CartMapper;
import com.mega._NY.cart.repository.CartRepository;
import com.mega._NY.cart.util.EntityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartMapper cartMapper;
    private final ItemCartService itemCartService;

    // 장바구니 생성
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    // 장바구니 정보 갱신
    public void refreshCart(long cartId) {
        Cart cart = findVerifiedCart(cartId);
        itemCartService.updateCartTotals(cart);
        cart.setTotalPrice(cart.getTotalPrice());
        cart.setTotalDiscountPrice(cart.getTotalDiscountPrice());
        cart.setTotalItems(cart.getTotalItems());
        cartRepository.save(cart);
    }

    // 현재 사용자의 장바구니 찾기
    public Cart findMyCart() {
        Long userId = userService.getLoginUser().getId();
        return cartRepository.findByUserId(userId);
    }

    // 현재 사용자의 장바구니 DTO 반환
    public CartDTO findMyCartDTO() {
        Cart cart = findMyCart();
        return cartMapper.toDTO(cart);
    }

    public Cart findVerifiedCart(long cartId) {
        return EntityUtils.findVerifiedEntity(cartRepository, cartId, ExceptionCode.CART_NOT_FOUND);
    }

    // 장바구니 비우기
    public void clearCart(User user) {
        Cart cart = findMyCart();
        if (cart != null) {
            // ItemCart 엔티티들을 모두 제거
            itemCartService.removeAllItemCartsFromCart(cart);

            // Cart 엔티티 업데이트
            cart.setTotalPrice(0);
            cart.setTotalDiscountPrice(0);
            cart.setTotalItems(0);
            cartRepository.save(cart);
        }
    }

}
