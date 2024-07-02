package com.mega._NY.cart.repository;

import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {

    ItemCart findByCartAndItem(Cart cart, Item item);

    List<ItemCart> findAllByCart(Cart cart);

    List<ItemCart> findAllByCartAndBuyNow(Cart cart, boolean buyNow);

    // 특정 사용자의 모든 장바구니를 페이지네이션하여 조회
    Page<ItemCart> findAllByCartUserId(Long userId, Pageable pageable);

}
