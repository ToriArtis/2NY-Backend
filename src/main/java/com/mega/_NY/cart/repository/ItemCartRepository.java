package com.mega._NY.cart.repository;

import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {

    ItemCart findByCartAndItem(Cart cart, Item item);

    List<ItemCart> findAllByCart(Cart cart);

    List<ItemCart> findAllByCartAndBuyNow(Cart cart, boolean buyNow);

    void deleteByItemAndCart(Item item, Cart cart); // 주문시 장바구니에서 아이템 삭제

}
