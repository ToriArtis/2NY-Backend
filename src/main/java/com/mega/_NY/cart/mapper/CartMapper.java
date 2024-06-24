package com.mega._NY.cart.mapper;

import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemCartMapper.class})
public interface CartMapper {

    // Cart 엔티티를 CartDTO로 변환
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "itemCarts", target = "itemCarts")
    CartDTO toDTO(Cart cart);

    // CartDTO를 Cart 엔티티로 변환
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "itemCarts", ignore = true)
    Cart toEntity(CartDTO cartDTO);

}
