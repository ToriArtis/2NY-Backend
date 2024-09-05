package com.mega._NY.item.mapper;

import com.mega._NY.item.dto.ItemDTO;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.entity.Item.ItemBuilder;
import com.mega._NY.item.entity.ItemColor;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-05T11:16:45+0900",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class ItemMapperImpl implements ItemMapper {

    @Override
    public Item toEntity(ItemDTO itemDTO) {
        if ( itemDTO == null ) {
            return null;
        }

        ItemBuilder item = Item.builder();

        if ( itemDTO.getColor() != null ) {
            item.color( stringToItemColor( itemDTO.getColor().name() ) );
        }
        item.title( itemDTO.getTitle() );
        item.content( itemDTO.getContent() );
        List<String> list = itemDTO.getThumbnail();
        if ( list != null ) {
            item.thumbnail( new ArrayList<String>( list ) );
        }
        List<String> list1 = itemDTO.getDescriptionImage();
        if ( list1 != null ) {
            item.descriptionImage( new ArrayList<String>( list1 ) );
        }
        item.price( itemDTO.getPrice() );
        item.discountPrice( itemDTO.getDiscountPrice() );
        item.discountRate( itemDTO.getDiscountRate() );
        item.sales( itemDTO.getSales() );
        item.size( itemDTO.getSize() );
        item.category( itemDTO.getCategory() );

        return item.build();
    }

    @Override
    public ItemDTO toDTO(Item item) {
        if ( item == null ) {
            return null;
        }

        ItemDTO itemDTO = new ItemDTO();

        if ( item.getColor() != null ) {
            itemDTO.setColor( Enum.valueOf( ItemColor.class, itemColorToString( item.getColor() ) ) );
        }
        itemDTO.setItemId( item.getItemId() );
        List<String> list = item.getThumbnail();
        if ( list != null ) {
            itemDTO.setThumbnail( new ArrayList<String>( list ) );
        }
        List<String> list1 = item.getDescriptionImage();
        if ( list1 != null ) {
            itemDTO.setDescriptionImage( new ArrayList<String>( list1 ) );
        }
        itemDTO.setTitle( item.getTitle() );
        itemDTO.setContent( item.getContent() );
        itemDTO.setSales( item.getSales() );
        itemDTO.setPrice( item.getPrice() );
        itemDTO.setSize( item.getSize() );
        itemDTO.setCategory( item.getCategory() );
        itemDTO.setDiscountRate( item.getDiscountRate() );
        itemDTO.setDiscountPrice( item.getDiscountPrice() );
        itemDTO.setCreatedAt( item.getCreatedAt() );
        itemDTO.setUpdatedAt( item.getUpdatedAt() );

        return itemDTO;
    }
}
