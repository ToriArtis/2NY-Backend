package com.mega._NY.orders.mapper;

import com.mega._NY.item.entity.Item;
import com.mega._NY.orders.dto.ItemOrderDTO;
import com.mega._NY.orders.entity.ItemOrders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemOrdersMapper {

    @Mapping(target = "item", source = "itemId", qualifiedByName = "itemIdToItem")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "buyNow", ignore = true)
    ItemOrders itemOrderDtoToItemOrder(ItemOrderDTO itemOrderDto);

    @Mapping(target = "itemId", source = "item.itemId")
    @Mapping(target = "itemTitle", source = "item.title")
    @Mapping(target = "price", source = "item.price")
    @Mapping(target = "totalPrice", expression = "java(itemOrders.getQuantity() * itemOrders.getItem().getPrice())")
    @Mapping(target = "size", source = "item.size")
    @Mapping(target = "color", source = "item.color")
    @Mapping(target = "discountRate", source = "item.discountRate")
    @Mapping(target = "discountPrice", source = "item.discountPrice")
    @Mapping(source = "item.thumbnail", target = "thumbnail")
    ItemOrderDTO itemOrderToItemOrderDto(ItemOrders itemOrders);

    List<ItemOrders> itemOrderDtosToItemOrders(List<ItemOrderDTO> itemOrderDtos);

    List<ItemOrderDTO> itemOrdersToItemOrderDtos(List<ItemOrders> itemOrders);

    @Named("itemIdToItem")
    default Item itemIdToItem(Long itemId) {
        if (itemId == null) {
            return null;
        }
        Item item = new Item();
        item.setItemId(itemId);
        return item;
    }

}
