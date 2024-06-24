package com.mega._NY.service;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.repository.CartRepository;
import com.mega._NY.cart.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CartServiceTests {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Test
    public void testFindMyCart() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");

        Cart cart = new Cart();
        cart.setUser(user);
        Cart savedCart = cartRepository.save(cart);

        // When
        Cart foundCart = cartService.findMyCart();

        // Then
        System.out.println("Found Cart ID: " + foundCart.getCartId());
        System.out.println("Found Cart User Email: " + foundCart.getUser().getEmail());
        // 여기서는 찾은 카트의 ID와 사용자 이메일을 출력합니다.
        // 실제 검증은 하지 않습니다.
    }

    @Test
    public void testRefreshCart() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalItems(5);
        cart.setTotalPrice(1000);
        Cart savedCart = cartRepository.save(cart);

        // When
        cartService.refreshCart(savedCart.getCartId());

        // Then
        Cart refreshedCart = cartRepository.findById(savedCart.getCartId()).orElse(null);
        if (refreshedCart != null) {
            System.out.println("Refreshed Cart Total Items: " + refreshedCart.getTotalItems());
            System.out.println("Refreshed Cart Total Price: " + refreshedCart.getTotalPrice());
        } else {
            System.out.println("Refreshed Cart not found");
        }
        // 여기서는 새로 고침된 카트의 총 아이템 수와 총 가격을 출력합니다.
        // 실제 검증은 하지 않습니다.
    }

}
