package com.mega._NY.repository;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.repository.CartRepository;
import com.mega._NY.cart.repository.ItemCartRepository;
import com.mega._NY.item.entity.Item;
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
        cart.setUser(savedUser);

        Cart savedCart = cartRepository.save(cart);

        System.out.println("Saved Cart ID: " + savedCart.getCartId());
    }

}
