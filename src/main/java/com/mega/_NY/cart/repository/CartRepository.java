package com.mega._NY.cart.repository;

import com.mega._NY.cart.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);

    // 특정 사용자의 모든 장바구니를 페이지네이션하여 조회
    Page<Cart> findAllByUserId(Long userId, Pageable pageable);
}
