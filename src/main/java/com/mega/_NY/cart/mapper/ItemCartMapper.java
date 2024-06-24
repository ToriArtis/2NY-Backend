package com.mega._NY.cart.mapper;

import com.mega._NY.auth.entity.User;
import com.mega._NY.auth.service.UserService;
import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.ItemCart;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemCartMapper {

    default ItemCart itemCartPostDtoToItemCart(long itemId, UserService userService,
//                                               ItemService itemService,
                                               ItemCartDTO.Post itemCartPostDto) {
        User user = userService.getLoginUser();
        return ItemCart.builder()
                .quantity(itemCartPostDto.getQuantity())
                .buyNow(true)
                .cart(user.getCart())
//                .item(itemService.findVerifiedItem(itemId))
                .build();
    }

    default ItemCartDTO.Response itemCartToItemCartResponseDto(ItemCart itemCart
//                                                               ItemMapper itemMapper
                                                               ) {
        return ItemCartDTO.Response.builder()
                .itemCartId(itemCart.getItemCartId())
                .quantity(itemCart.getQuantity())
                .buyNow(itemCart.isBuyNow())
//                .item(itemMapper.itemToItemSimpleResponseDto(itemCart.getItem()))
//                .createdAt(itemCart.getCreatedAt())
//                .updatedAt(itemCart.getUpdatedAt())
                .build();
    }

    default List<ItemCartDTO.Response> itemCartsToItemCartResponseDtos(List<ItemCart> itemCarts
//                                                                       ItemMapper itemMapper,
                                                                       ) {
        if(itemCarts == null) return null;

        List<ItemCartDTO.Response> itemCartResponseDtos = new ArrayList<>(itemCarts.size());

//        for(ItemCart itemCart : itemCarts) {
//            itemCartResponseDtos.add(itemCartToItemCartResponseDto(itemMapper, itemCart));
//        }

        return itemCartResponseDtos;
    }

}
