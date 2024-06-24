package com.mega._NY.service;

import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.repository.ItemCartRepository;
import com.mega._NY.cart.service.ItemCartService;
import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
public class ItemCartServiceTests {

    @Autowired
    private ItemCartService itemCartService;

    @Autowired
    private ItemCartRepository itemCartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testAddItemCart() {
        // 장바구니에 상품을 추가하는 테스트
        ItemCartDTO itemCartDTO = new ItemCartDTO();
        itemCartDTO.setQuantity(1);
        Long itemId = 1L;
        Item item = itemRepository.findById(itemId).orElse(new Item());
        Cart cart = new Cart();

        ItemCartDTO createdItemCart = itemCartService.addItemCart(itemCartDTO, itemId, cart);

        if (createdItemCart != null) {
            log.info("ItemCart created with ID: {}", createdItemCart.getItemCartId());
        } else {
            log.info("Failed to create ItemCart");
        }

        ItemCart retrievedItemCart = itemCartRepository.findById(createdItemCart.getItemCartId()).orElse(null);
        log.info("ItemCart from repository: {}", retrievedItemCart);
    }

    @Test
    public void testUpDownItemCart() {
        // 장바구니 상품 수량을 변경하는 테스트
        ItemCart itemCart = new ItemCart();
        itemCart.setQuantity(1);
        itemCart = itemCartRepository.save(itemCart);

        ItemCartDTO updatedItemCart = itemCartService.updownItemCart(itemCart.getItemCartId(), 1);
        log.info("Updated ItemCart: {}", updatedItemCart);

        ItemCart retrievedItemCart = itemCartRepository.findById(updatedItemCart.getItemCartId()).orElse(null);
        log.info("ItemCart from repository: {}", retrievedItemCart);
    }

    @Test
    public void testDeleteItemCart() {
        // 장바구니에서 상품을 삭제하는 테스트
        ItemCart itemCart = new ItemCart();
        Cart cart = new Cart();
        itemCart.setCart(cart);
        itemCart = itemCartRepository.save(itemCart);

        long cartId = itemCartService.deleteItemCart(itemCart.getItemCartId());
        log.info("Deleted ItemCart with Cart ID: {}", cartId);

        ItemCart retrievedItemCart = itemCartRepository.findById(itemCart.getItemCartId()).orElse(null);
        log.info("ItemCart should be null: {}", retrievedItemCart);
    }

}
