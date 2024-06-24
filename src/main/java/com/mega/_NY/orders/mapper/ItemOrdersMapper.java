package com.mega._NY.orders.mapper;

import com.mega._NY.orders.dto.ItemOrderDTO;
import com.mega._NY.orders.entity.ItemOrders;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ItemOrdersMapper {

    // DTO를 엔티티로 변환
    ItemOrders itemOrderDtoToItemOrder(ItemOrderDTO itemOrderDto);

    // DTO 리스트를 엔티티 리스트로 변환
    default List<ItemOrders> itemOrderDtosToItemOrders(List<ItemOrderDTO> itemOrderDtos) {
        return itemOrderDtos.stream()
                .map(this::itemOrderDtoToItemOrder)
                .collect(Collectors.toList());
    }

}
