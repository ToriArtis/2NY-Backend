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
        User savedUser = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUserId(savedUser.getId());  // UserId 설정

        Cart savedCart = cartRepository.save(cart);

        Cart foundCart = cartRepository.findByUserId(savedUser.getId());

    }

}
