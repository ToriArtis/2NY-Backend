package com.mega._NY.controller;

import com.mega._NY.cart.controller.ItemCartController;
import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.service.CartService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@SpringBootTest
@Transactional
public class ItemCartControllerTests {

    @Autowired
    private ItemCartController itemCartController;

    @Autowired
    private CartService cartService;


    @Test
    public void testPostItemCart() {
        // 장바구니에 상품을 추가하는 테스트
        Cart cart = cartService.findMyCart();
        ItemCartDTO itemCartDTO = new ItemCartDTO();
        itemCartDTO.setQuantity(1);

        ResponseEntity<ItemCartDTO> response = itemCartController.postItemCart(itemCartDTO, 1L);

        log.info("Response: {}", response.getBody());

        if (response.getStatusCode() != HttpStatus.CREATED) {
            log.error("Expected status CREATED but got {}", response.getStatusCode());
            throw new AssertionError("Expected status CREATED but got " + response.getStatusCode());
        }
        if (response.getBody() == null) {
            log.error("Expected body not to be null");
            throw new AssertionError("Expected body not to be null");
        }
        if (response.getBody().getItemCartId() == null) {
            log.error("Expected itemCartId not to be null");
            throw new AssertionError("Expected itemCartId not to be null");
        }
    }

    @Test
    public void testUpDownItemCart() {
        // 장바구니 상품 수량을 변경하는 테스트
        ResponseEntity<ItemCartDTO> response = itemCartController.upDownItemCart(1L, 1);

        log.info("Response: {}", response.getBody());

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Expected status OK but got {}", response.getStatusCode());
            throw new AssertionError("Expected status OK but got " + response.getStatusCode());
        }
        if (response.getBody() == null) {
            log.error("Expected body not to be null");
            throw new AssertionError("Expected body not to be null");
        }
        if (response.getBody().getItemCartId() == null) {
            log.error("Expected itemCartId not to be null");
            throw new AssertionError("Expected itemCartId not to be null");
        }
    }

    @Test
    public void testDeleteItemCart() {
        // 장바구니에서 상품을 삭제하는 테스트
        ResponseEntity<Void> response = itemCartController.deleteItemCart(1L);

        log.info("Response status: {}", response.getStatusCode());

        if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
            log.error("Expected status NO_CONTENT but got {}", response.getStatusCode());
            throw new AssertionError("Expected status NO_CONTENT but got " + response.getStatusCode());
        }
    }

}
