package com.mega._NY.item.mapper;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.entity.ItemColor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "color", target = "color", qualifiedByName = "stringToItemColor")
    Item toEntity(ItemDTO itemDTO);

    @Mapping(source = "color", target = "color", qualifiedByName = "itemColorToString")
    ItemDTO toDTO(Item item);

    @Named("stringToItemColor")
    default ItemColor stringToItemColor(String color) {
        return color != null ? ItemColor.valueOf(color.toUpperCase()) : null;
    }

    @Named("itemColorToString")
    default String itemColorToString(ItemColor itemColor) {
        return itemColor != null ? itemColor.name() : null;
    }
}