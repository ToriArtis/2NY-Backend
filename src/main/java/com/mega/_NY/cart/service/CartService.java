package com.mega._NY.cart.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.mapper.CartMapper;
import com.mega._NY.cart.repository.CartRepository;
import com.mega._NY.cart.repository.ItemCartRepository;
import com.mega._NY.cart.util.EntityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ItemCartService itemCartService;
    private final ItemCartRepository itemCartRepository;

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
    public Cart findMyCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new BusinessLogicException(ExceptionCode.CART_NOT_FOUND);
        }
        return cart;
    }

    // 현재 사용자의 장바구니 DTO 반환
    public CartDTO findMyCartDTO(Long userId) {
        Cart cart = findMyCart(userId);
        return cartMapper.toDTO(cart);
    }

    // 사용자의 장바구니 목록 조회 (페이지네이션)
    public Page<ItemCart> findCartItems(Long userId, int page, int size) {
        return itemCartRepository.findAllByCartUserId(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "itemCartId")));
    }

    public Cart findVerifiedCart(long cartId) {
        return EntityUtils.findVerifiedEntity(cartRepository, cartId, ExceptionCode.CART_NOT_FOUND);
    }

    // 장바구니 비우기
    public void clearCart(User user) {
        Cart cart = findMyCart(user.getId());
        if (cart != null) {
            // ItemCart 엔티티들을 모두 제거
            itemCartService.removeAllItemCartsFromCart(cart);

            // Cart 엔티티 업데이트
            cart.setTotalPrice(0);
            cart.setTotalDiscountPrice(0);
            cart.setTotalItems(0);
            cartRepository.save(cart);
        } else {
            throw new BusinessLogicException(ExceptionCode.CART_NOT_FOUND);
        }
    }

}
