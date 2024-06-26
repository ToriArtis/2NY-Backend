package com.mega._NY.repository;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveCart() {
        User user = new User();
        user.setEmail("test100@naver.com");

        // User를 먼저 저장
        User savedUser = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(savedUser);  // 이 부분에서 양방향 관계가 설정됩니다.

        Cart savedCart = cartRepository.save(cart);

        // User를 다시 저장하여 Cart ID를 반영  + 추가된 부분 (추가 후 user에 cart_id 잘 들어감)
        userRepository.save(savedUser);

        System.out.println("Saved Cart ID: " + savedCart.getCartId());
        System.out.println("User's Cart ID: " + savedUser.getCart().getCartId());
    }

}
