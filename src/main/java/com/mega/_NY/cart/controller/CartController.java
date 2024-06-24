package com.mega._NY.cart.controller;

import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.mapper.CartMapper;
import com.mega._NY.cart.mapper.ItemCartMapper;
import com.mega._NY.cart.service.CartService;
import com.mega._NY.cart.service.ItemCartService;
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

    // 현재 사용자의 장바구니 조회
    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        CartDTO cartDTO = cartService.findMyCartDTO();
        return ResponseEntity.ok(cartDTO);
    }

}
