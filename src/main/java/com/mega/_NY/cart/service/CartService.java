package com.mega._NY.cart.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.mapper.CartMapper;
import com.mega._NY.cart.repository.CartRepository;
import com.mega._NY.cart.util.EntityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartMapper cartMapper;
    private final ItemCartService itemCartService;

    // 장바구니 정보 갱신
    public void refreshCart(long cartId) {
        Cart cart = findVerifiedCart(cartId);
        itemCartService.updateCartTotals(cart);
        cartRepository.save(cart);
    }

    // 현재 사용자의 장바구니 찾기
    public Cart findMyCart() {
        User user = userService.getLoginUser();
        return cartRepository.findByUser(user);
    }

    // 현재 사용자의 장바구니 DTO 반환
    public CartDTO findMyCartDTO() {
        Cart cart = findMyCart();
        return cartMapper.toDTO(cart);
    }

    public Cart findVerifiedCart(long cartId) {
        return EntityUtils.findVerifiedEntity(cartRepository, cartId, ExceptionCode.CART_NOT_FOUND);
    }


}
