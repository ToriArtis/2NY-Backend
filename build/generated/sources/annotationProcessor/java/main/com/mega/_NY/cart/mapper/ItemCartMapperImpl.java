package com.mega._NY.cart.mapper;

import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.entity.ItemCart.ItemCartBuilder;
import com.mega._NY.item.entity.Item;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-26T17:59:19+0900",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class ItemCartMapperImpl implements ItemCartMapper {

    @Override
    public ItemCartDTO toDTO(ItemCart itemCart) {
        if ( itemCart == null ) {
            return null;
        }

        ItemCartDTO itemCartDTO = new ItemCartDTO();

        itemCartDTO.setItemId( itemCartItemItemId( itemCart ) );
        itemCartDTO.setCartId( itemCartCartCartId( itemCart ) );
        itemCartDTO.setPrice( itemCartItemPrice( itemCart ) );
        itemCartDTO.setDiscountRate( itemCartItemDiscountRate( itemCart ) );
        itemCartDTO.setDiscountPrice( itemCartItemDiscountPrice( itemCart ) );
        List<String> thumbnail = itemCartItemThumbnail( itemCart );
        List<String> list = thumbnail;
        if ( list != null ) {
            itemCartDTO.setThumbnail( new ArrayList<String>( list ) );
        }
        itemCartDTO.setItemTitle( itemCartItemTitle( itemCart ) );
        itemCartDTO.setItemCartId( itemCart.getItemCartId() );
        itemCartDTO.setQuantity( itemCart.getQuantity() );

        return itemCartDTO;
    }

    @Override
    public ItemCart toEntity(ItemCartDTO itemCartDTO) {
        if ( itemCartDTO == null ) {
            return null;
        }

        ItemCartBuilder itemCart = ItemCart.builder();

        itemCart.itemCartId( itemCartDTO.getItemCartId() );
        itemCart.quantity( itemCartDTO.getQuantity() );

        return itemCart.build();
    }

    private Long itemCartItemItemId(ItemCart itemCart) {
        if ( itemCart == null ) {
            return null;
        }
        Item item = itemCart.getItem();
        if ( item == null ) {
            return null;
        }
        Long itemId = item.getItemId();
        if ( itemId == null ) {
            return null;
        }
        return itemId;
    }

    private Long itemCartCartCartId(ItemCart itemCart) {
        if ( itemCart == null ) {
            return null;
        }
        Cart cart = itemCart.getCart();
        if ( cart == null ) {
            return null;
        }
        Long cartId = cart.getCartId();
        if ( cartId == null ) {
            return null;
        }
        return cartId;
    }

    private int itemCartItemPrice(ItemCart itemCart) {
        if ( itemCart == null ) {
            return 0;
        }
        Item item = itemCart.getItem();
        if ( item == null ) {
            return 0;
        }
        int price = item.getPrice();
        return price;
    }

    private int itemCartItemDiscountRate(ItemCart itemCart) {
        if ( itemCart == null ) {
            return 0;
        }
        Item item = itemCart.getItem();
        if ( item == null ) {
            return 0;
        }
        int discountRate = item.getDiscountRate();
        return discountRate;
    }

    private int itemCartItemDiscountPrice(ItemCart itemCart) {
        if ( itemCart == null ) {
            return 0;
        }
        Item item = itemCart.getItem();
        if ( item == null ) {
            return 0;
        }
        int discountPrice = item.getDiscountPrice();
        return discountPrice;
    }

    private List<String> itemCartItemThumbnail(ItemCart itemCart) {
        if ( itemCart == null ) {
            return null;
        }
        Item item = itemCart.getItem();
        if ( item == null ) {
            return null;
        }
        List<String> thumbnail = item.getThumbnail();
        if ( thumbnail == null ) {
            return null;
        }
        return thumbnail;
    }

    private String itemCartItemTitle(ItemCart itemCart) {
        if ( itemCart == null ) {
            return null;
        }
        Item item = itemCart.getItem();
        if ( item == null ) {
            return null;
        }
        String title = item.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}
