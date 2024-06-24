package com.mega._NY.cart.mapper;

import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.service.CartService;
import com.mega._NY.cart.service.ItemCartService;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    default CartDTO cartToCartResponseDto(Cart cart, CartService cartService, ItemCartService itemCartService,
//                                          ItemMapper itemMapper,
                                          ItemCartMapper itemCartMapper) {
        CartDTO cartResponseDto = new CartDTO();
        cartResponseDto.setCartId(cart.getCartId());

        List<ItemCart> itemCarts = itemCartService.findItemCarts(cart); // 목록은 체크 + 언체크 모두 조회

//        itemCartMapper.itemCartsToItemCartResponseDtos(itemMapper, itemCarts);
//        cartResponseDto.setItemCarts(new MultiResponseDto<>(
//                itemCartMapper.itemCartsToItemCartResponseDtos(itemMapper, itemCarts))
//        );

        cartResponseDto.setTotalPrice(cart.getTotalPrice());
        cartResponseDto.setTotalItems(cart.getTotalItems());

        cartResponseDto.setTotalDiscountPrice(cartService.countTotalDiscountPrice(cart.getCartId()));
        cartResponseDto.setExpectPrice(cartResponseDto.getTotalPrice() - cartResponseDto.getTotalDiscountPrice());

        return cartResponseDto;
    }

}
