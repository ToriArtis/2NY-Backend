package com.mega._NY.orders.mapper;

import com.mega._NY.item.entity.Item;
import com.mega._NY.item.entity.ItemColor;
import com.mega._NY.item.entity.ItemSize;
import com.mega._NY.orders.dto.ItemOrderDTO;
import com.mega._NY.orders.dto.ItemOrderDTO.ItemOrderDTOBuilder;
import com.mega._NY.orders.entity.ItemOrders;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-04T10:57:28+0900",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class ItemOrdersMapperImpl implements ItemOrdersMapper {

    @Override
    public ItemOrders itemOrderDtoToItemOrder(ItemOrderDTO itemOrderDto) {
        if ( itemOrderDto == null ) {
            return null;
        }

        ItemOrders itemOrders = new ItemOrders();

        itemOrders.setItem( itemIdToItem( itemOrderDto.getItemId() ) );
        itemOrders.setItemOrderId( itemOrderDto.getItemOrderId() );
        itemOrders.setQuantity( itemOrderDto.getQuantity() );
        itemOrders.setPrice( itemOrderDto.getPrice() );
        itemOrders.setTotalPrice( itemOrderDto.getTotalPrice() );
        itemOrders.setDiscountPrice( itemOrderDto.getDiscountPrice() );

        return itemOrders;
    }

    @Override
    public ItemOrderDTO itemOrderToItemOrderDto(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return null;
        }

        ItemOrderDTOBuilder itemOrderDTO = ItemOrderDTO.builder();

        itemOrderDTO.itemId( itemOrdersItemItemId( itemOrders ) );
        itemOrderDTO.itemTitle( itemOrdersItemTitle( itemOrders ) );
        itemOrderDTO.price( itemOrdersItemPrice( itemOrders ) );
        itemOrderDTO.size( itemOrdersItemSize( itemOrders ) );
        itemOrderDTO.color( itemOrdersItemColor( itemOrders ) );
        itemOrderDTO.discountRate( itemOrdersItemDiscountRate( itemOrders ) );
        itemOrderDTO.discountPrice( itemOrdersItemDiscountPrice( itemOrders ) );
        List<String> thumbnail = itemOrdersItemThumbnail( itemOrders );
        List<String> list = thumbnail;
        if ( list != null ) {
            itemOrderDTO.thumbnail( new ArrayList<String>( list ) );
        }
        itemOrderDTO.itemOrderId( itemOrders.getItemOrderId() );
        itemOrderDTO.quantity( itemOrders.getQuantity() );

        itemOrderDTO.totalPrice( itemOrders.getQuantity() * itemOrders.getItem().getPrice() );

        return itemOrderDTO.build();
    }

    @Override
    public List<ItemOrders> itemOrderDtosToItemOrders(List<ItemOrderDTO> itemOrderDtos) {
        if ( itemOrderDtos == null ) {
            return null;
        }

        List<ItemOrders> list = new ArrayList<ItemOrders>( itemOrderDtos.size() );
        for ( ItemOrderDTO itemOrderDTO : itemOrderDtos ) {
            list.add( itemOrderDtoToItemOrder( itemOrderDTO ) );
        }

        return list;
    }

    @Override
    public List<ItemOrderDTO> itemOrdersToItemOrderDtos(List<ItemOrders> itemOrders) {
        if ( itemOrders == null ) {
            return null;
        }

        List<ItemOrderDTO> list = new ArrayList<ItemOrderDTO>( itemOrders.size() );
        for ( ItemOrders itemOrders1 : itemOrders ) {
            list.add( itemOrderToItemOrderDto( itemOrders1 ) );
        }

        return list;
    }

    private Long itemOrdersItemItemId(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return null;
        }
        Item item = itemOrders.getItem();
        if ( item == null ) {
            return null;
        }
        Long itemId = item.getItemId();
        if ( itemId == null ) {
            return null;
        }
        return itemId;
    }

    private String itemOrdersItemTitle(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return null;
        }
        Item item = itemOrders.getItem();
        if ( item == null ) {
            return null;
        }
        String title = item.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private int itemOrdersItemPrice(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return 0;
        }
        Item item = itemOrders.getItem();
        if ( item == null ) {
            return 0;
        }
        int price = item.getPrice();
        return price;
    }

    private ItemSize itemOrdersItemSize(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return null;
        }
        Item item = itemOrders.getItem();
        if ( item == null ) {
            return null;
        }
        ItemSize size = item.getSize();
        if ( size == null ) {
            return null;
        }
        return size;
    }

    private ItemColor itemOrdersItemColor(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return null;
        }
        Item item = itemOrders.getItem();
        if ( item == null ) {
            return null;
        }
        ItemColor color = item.getColor();
        if ( color == null ) {
            return null;
        }
        return color;
    }

    private int itemOrdersItemDiscountRate(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return 0;
        }
        Item item = itemOrders.getItem();
        if ( item == null ) {
            return 0;
        }
        int discountRate = item.getDiscountRate();
        return discountRate;
    }

    private int itemOrdersItemDiscountPrice(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return 0;
        }
        Item item = itemOrders.getItem();
        if ( item == null ) {
            return 0;
        }
        int discountPrice = item.getDiscountPrice();
        return discountPrice;
    }

    private List<String> itemOrdersItemThumbnail(ItemOrders itemOrders) {
        if ( itemOrders == null ) {
            return null;
        }
        Item item = itemOrders.getItem();
        if ( item == null ) {
            return null;
        }
        List<String> thumbnail = item.getThumbnail();
        if ( thumbnail == null ) {
            return null;
        }
        return thumbnail;
    }
}
