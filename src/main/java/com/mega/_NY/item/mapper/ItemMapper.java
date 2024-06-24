package com.mega._NY.item.mapper;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDTO toDTO(Item item);

    Item toEntity(ItemDTO itemDTO);
}