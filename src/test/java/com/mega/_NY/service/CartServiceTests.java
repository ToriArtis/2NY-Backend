package com.mega._NY.service;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.repository.CartRepository;
import com.mega._NY.cart.service.CartService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@Slf4j
@SpringBootTest
//@Transactional
public class CartServiceTests {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindMyCart() {
        // 현재 사용자의 장바구니를 찾는 테스트
        // 기존 사용자 조회
        Optional<User> optionalUser = userRepository.findByEmail("test100@naver.com");
        if (!optionalUser.isPresent()) {
            log.info("테스트를 위한 기존 사용자가 없습니다.");
            throw new RuntimeException("테스트를 위한 기존 사용자가 없습니다.");
        }
        Long existingUser = optionalUser.get().getId();

        // 사용자의 기존 카트 조회
        Cart existingCart = cartRepository.findByUserId(existingUser);
        if (existingCart == null) {
            log.info("사용자의 카트가 없습니다.");
            throw new RuntimeException("사용자의 카트가 없습니다.");
        }
        log.info("Found existing cart with ID: {}", existingCart.getCartId());

        // 사용자로 장바구니 조회
        Cart foundCart = cartRepository.findByUserId(existingUser);
        log.info("Found cart by user: {}", foundCart != null ? foundCart.getCartId() : "null");

        // 조회된 장바구니가 null이 아닌지 확인
        Assert.notNull(foundCart, "Found cart should not be null");
        log.info("Assert passed: Found cart is not null");

        // 조회된 장바구니 ID가 원래 장바구니 ID와 동일한지 확인
        Assert.isTrue(existingCart.getCartId().equals(foundCart.getCartId()), "Cart IDs should be equal");
        log.info("Assert passed: Cart IDs are equal");

        log.info("testReadCart completed successfully");
    }

    @Test
    public void testRefreshCart() {
        // 장바구니 정보를 갱신하는 테스트
        Cart cart = cartRepository.findById(1L).orElse(null);
        cart = cartRepository.save(cart);
        cartService.refreshCart(cart.getCartId());

        Cart refreshedCart = cartService.findVerifiedCart(cart.getCartId());
        log.info("Refreshed cart: {}", refreshedCart);

    }

    @Test
    public void testFindVerifiedCart() {
        // 유효한 장바구니를 찾는 테스트
        Cart cart = new Cart();
        cart = cartRepository.save(cart);

        Cart foundCart = cartService.findVerifiedCart(cart.getCartId());
        log.info("Verified cart: {}", foundCart);
    }

}
