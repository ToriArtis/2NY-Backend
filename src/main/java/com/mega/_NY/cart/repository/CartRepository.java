package com.mega._NY.cart.repository;

import com.mega._NY.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);

}
