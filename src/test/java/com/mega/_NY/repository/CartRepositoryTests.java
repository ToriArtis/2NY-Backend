package com.mega._NY.repository;

import com.mega._NY.auth.entity.User;
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

    @Test
    public void testSaveCart() {
        User user = new User();
        user.setEmail("test50@naver.com");

        Cart cart = new Cart();
        cart.setUser(user);

        Cart savedCart = cartRepository.save(cart);

        System.out.println("Saved Cart ID: " + savedCart.getCartId());
    }

}
