package com.mega._NY.service;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.repository.UserRepository;
import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.repository.CartRepository;
import com.mega._NY.cart.repository.ItemCartRepository;
import com.mega._NY.cart.service.ItemCartService;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
//@Transactional
public class ItemCartServiceTests {

    @Autowired
    private ItemCartService itemCartService;

    @Autowired
    private ItemCartRepository itemCartRepository;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    public void testAddItemCart() {
        // 사용자 조회
        User user = userRepository.findById(1L).orElse(null);
        user = userRepository.save(user);

        // 카트 조회
        Cart cart = cartRepository.findById(1L).orElse(null);
        cart = cartRepository.save(cart);

        // 상품 생성 및 저장
        Item item = new Item();
        item.setTitle("Test Item");
        item.setPrice(1000);
        item.setDiscountRate(10);
        Item savedItem = itemRepository.save(item);

        Item item2 = new Item();
        item2.setTitle("Test Item");
        item2.setPrice(1500);
        item2.setDiscountRate(20);
        Item savedItem2 = itemRepository.save(item2);

        // 장바구니에 상품을 추가하는 테스트
        ItemCartDTO itemCartDTO = new ItemCartDTO();
        itemCartDTO.setQuantity(1);
        Long itemId = savedItem.getItemId();
        Long itemId2 = savedItem2.getItemId();

        ItemCartDTO createdItemCart = itemCartService.addItemCart(itemCartDTO, itemId, cart);
        ItemCartDTO createdItemCart2 = itemCartService.addItemCart(itemCartDTO, itemId2, cart);

        if (createdItemCart != null) {
            log.info("ItemCart created with ID: {}", createdItemCart.getItemCartId());
        } else {
            log.info("Failed to create ItemCart");
        }

        ItemCart retrievedItemCart = itemCartRepository.findById(createdItemCart.getItemCartId()).orElse(null);
        ItemCart retrievedItemCart2 = itemCartRepository.findById(createdItemCart2.getItemCartId()).orElse(null);
        log.info("ItemCart from repository: {}", retrievedItemCart);
        log.info("ItemCart from repository: {}", retrievedItemCart2);
    }

    @Test
    public void testUpDownItemCart() {
        // 장바구니 상품 수량을 변경하는 테스트
        ItemCart itemCart = itemCartRepository.findById(1L).orElse(null);
        itemCart.setQuantity(2);
        itemCartRepository.save(itemCart);

        ItemCartDTO updatedItemCart = itemCartService.updownItemCart(itemCart.getItemCartId(), 3);
        log.info("Updated ItemCart: {}", updatedItemCart);

        ItemCart retrievedItemCart = itemCartRepository.findById(updatedItemCart.getItemCartId()).orElse(null);
        log.info("ItemCart from repository: {}", retrievedItemCart);
    }

    @Test
    public void testDeleteItemCart() {
        // 장바구니에서 상품을 삭제하는 테스트
        ItemCart itemCart = itemCartRepository.findById(1L).orElse(null);
        Cart cart = cartRepository.findById(1L).orElse(null);
        itemCart.setCart(cart);
        itemCart = itemCartRepository.save(itemCart);

        long cartId = itemCartService.deleteItemCart(itemCart.getItemCartId());
        log.info("Deleted ItemCart with Cart ID: {}", cartId);

        ItemCart retrievedItemCart = itemCartRepository.findById(itemCart.getItemCartId()).orElse(null);
        log.info("ItemCart should be null: {}", retrievedItemCart);
    }

}
