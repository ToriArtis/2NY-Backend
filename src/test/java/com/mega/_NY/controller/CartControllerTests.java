package com.mega._NY.controller;

import com.mega._NY.cart.controller.CartController;
import com.mega._NY.cart.dto.CartDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@SpringBootTest
@Transactional
public class CartControllerTests {

    @Autowired
    private CartController cartController;

    @Test
    public void testGetCart() {
        // 현재 사용자의 장바구니 정보를 가져오는 테스트
        ResponseEntity<CartDTO> response = cartController.getCart();

        log.info("Response: {}", response.getBody());

        // HTTP 상태 코드가 OK(200)인지 확인
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Expected status OK but got {}", response.getStatusCode());
            throw new AssertionError("Expected status OK but got " + response.getStatusCode());
        }

        // 응답 본문이 null이 아닌지 확인
        if (response.getBody() == null) {
            log.error("Expected body not to be null");
            throw new AssertionError("Expected body not to be null");
        }

        // 응답 본문의 cartId가 null이 아닌지 확인
        if (response.getBody().getCartId() == null) {
            log.error("Expected cartId not to be null");
            throw new AssertionError("Expected cartId not to be null");
        }
    }

}
