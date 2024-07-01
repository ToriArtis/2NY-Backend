package com.mega._NY.cart.controller;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserRoles;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.mapper.CartMapper;
import com.mega._NY.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    private final UserService userService;

    private boolean isUser() {
        User loginUser = userService.getLoginUser();
        return loginUser.getRoleSet().contains(UserRoles.USER);
    }

    // 장바구니 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Page<CartDTO>> getOrders(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long userId = userService.getLoginUser().getId();
        Page<Cart> carts = cartService.findCarts(userId, page, size);
        return ResponseEntity.ok(carts.map(cartMapper::toDTO));
    }

}
