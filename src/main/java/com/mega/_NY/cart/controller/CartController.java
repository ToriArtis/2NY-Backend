package com.mega._NY.cart.controller;

import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.mapper.CartMapper;
import com.mega._NY.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Log4j2
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    // 현재 사용자의 장바구니 조회
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable("userId") Long userId) {
        Cart cart = cartService.findVerifiedCart(userId);
        return ResponseEntity.ok(cartMapper.toDTO(cart));
    }

}
