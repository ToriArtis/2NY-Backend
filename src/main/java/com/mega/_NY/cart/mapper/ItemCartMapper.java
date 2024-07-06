package com.mega._NY.cart.mapper;

import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.ItemCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemCartMapper {

    // ItemCart 엔티티를 ItemCartDTO로 변환
    @Mapping(source = "item.itemId", target = "itemId")
    @Mapping(source = "cart.cartId", target = "cartId")
    @Mapping(source = "item.price", target = "price")
    @Mapping(source = "item.discountRate", target = "discountRate")
    @Mapping(source = "item.discountPrice", target = "discountPrice")
    @Mapping(source = "item.thumbnail", target = "thumbnail")
    @Mapping(source = "item.title", target = "itemTitle")
    ItemCartDTO toDTO(ItemCart itemCart);

    // ItemCartDTO를 ItemCart 엔티티로 변환
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "buyNow", ignore = true)
    ItemCart toEntity(ItemCartDTO itemCartDTO);

}
