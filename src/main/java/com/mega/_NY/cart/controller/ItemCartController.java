package com.mega._NY.cart.controller;

import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.mapper.ItemCartMapper;
import com.mega._NY.cart.service.CartService;
import com.mega._NY.cart.service.ItemCartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class ItemCartController {

    private final ItemCartService itemCartService;
    private final ItemCartMapper itemCartMapper;
//    private final ItemMapper itemMapper;
//    private final ItemService itemService;
    private final CartService cartService;
    private final UserService userService;

    @PostMapping("/{item-id}") // 장바구니 담기
    public ResponseEntity postItemCart(@Valid @RequestBody ItemCartDTO.Post itemCartPostDto,
                                       @PathVariable("item-id") @Positive long itemId) {

//        ItemCart itemCart = itemCartService.addItemCart(itemCartMapper.
//                itemCartPostDtoToItemCart(itemId, userService, itemService, itemCartPostDto));

        ItemCart itemCart = itemCartService.addItemCart(itemCartMapper.
                itemCartPostDtoToItemCart(itemId, userService, itemCartPostDto));

        cartService.refreshCart(itemCart.getCart().getCartId());

        ItemCartDTO.Response response = itemCartMapper.itemCartToItemCartResponseDto(itemCart);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/itemcarts/{itemcart-id}") // 장바구니 아이템 수량 변경
    public ResponseEntity upDownItemCart(@PathVariable("itemcart-id") @Positive long itemCartId,
                                         @RequestParam(value="upDown") int upDown) {

        ItemCart upDownItemCart = itemCartService.updownItemCart(itemCartId, upDown);
        cartService.refreshCart(upDownItemCart.getCart().getCartId());

        ItemCartDTO.Response response = itemCartMapper.itemCartToItemCartResponseDto(upDownItemCart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/itemcarts/exclude/{itemcart-id}") // 장바구니 아이템 체크/해제 - 디폴트는 해제 요청
    public ResponseEntity excludeItemCart(@PathVariable("itemcart-id") @Positive long itemCartId,
                                          @RequestParam(value="buyNow", defaultValue = "false") boolean buyNow) {
        ItemCart itemCart = itemCartService.excludeItemCart(itemCartId, buyNow);
        cartService.refreshCart(itemCart.getCart().getCartId());

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/itemcarts/{itemcart-id}") // 장바구니에서 특정 아이템 삭제
    public ResponseEntity deleteItemCart(@PathVariable("itemcart-id") @Positive long itemCartId) {
        long cartId = itemCartService.deleteItemCart(itemCartId);
        cartService.refreshCart(cartId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
