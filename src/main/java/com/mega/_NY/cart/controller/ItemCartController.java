package com.mega._NY.cart.controller;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.entity.UserRoles;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.service.CartService;
import com.mega._NY.cart.service.ItemCartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class ItemCartController {

    private final ItemCartService itemCartService;
    private final CartService cartService;
    private final UserService userService;

    private boolean isUser() {
        User loginUser = userService.getLoginUser();
        return loginUser.getRoleSet().contains(UserRoles.USER);
    }

    // 장바구니에 상품 추가
    @PostMapping("/{item-id}")
    public ResponseEntity<ItemCartDTO> postItemCart(@Valid @RequestBody ItemCartDTO itemCartDTO,
                                                    @PathVariable("item-id") @Positive Long itemId) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long userId = userService.getLoginUser().getId();
        Cart cart = cartService.findVerifiedCart(userId);   // 현재 사용자의 Cart를 가져옵니다.
        ItemCartDTO createdItemCart = itemCartService.addItemCart(itemCartDTO, itemId, cart);
        cartService.refreshCart(cart.getCartId());
        return new ResponseEntity<>(createdItemCart, HttpStatus.CREATED);
    }

    // 장바구니 상품 수량 변경
    @PutMapping("/itemcarts/{itemcart-id}")
    public ResponseEntity<ItemCartDTO> upDownItemCart(@PathVariable("itemcart-id") @Positive long itemCartId,
                                                      @RequestParam(value="upDown") int upDown) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long userId = userService.getLoginUser().getId();
        Cart cart = cartService.findMyCart(userId);   // 현재 사용자의 Cart를 가져옵니다.
        ItemCartDTO updatedItemCart = itemCartService.updownItemCart(itemCartId, upDown);
        cartService.refreshCart(updatedItemCart.getCartId());
        return new ResponseEntity<>(updatedItemCart, HttpStatus.OK);
    }

    // 장바구니 상품 구매 여부 변경
    @PutMapping("/{userId}/itemcarts/exclude/{itemcart-id}")
    public ResponseEntity<ItemCartDTO> excludeItemCart(@PathVariable("userId") Long userId, @PathVariable("itemcart-id") @Positive long itemCartId,
                                                       @RequestParam(value="buyNow", defaultValue = "false") boolean buyNow) {
        Cart cart = cartService.findVerifiedCart(userId);   // 현재 사용자의 Cart를 가져옵니다.
        ItemCartDTO itemCart = itemCartService.excludeItemCart(itemCartId, buyNow);
        cartService.refreshCart(itemCart.getCartId());
        return new ResponseEntity<>(itemCart, HttpStatus.OK);
    }

    // 장바구니에서 상품 삭제
    @DeleteMapping("/itemcarts/{itemcart-id}")
    public ResponseEntity<Void> deleteItemCart(@PathVariable("itemcart-id") @Positive long itemCartId) {
        if (!isUser()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Long userId = userService.getLoginUser().getId();
        Cart cart = cartService.findMyCart(userId);   // 현재 사용자의 Cart를 가져옵니다.
        long cartId = itemCartService.deleteItemCart(itemCartId);
        cartService.refreshCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
