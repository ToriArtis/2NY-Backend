package com.mega._NY.cart.mapper;

import com.mega._NY.auth.entity.User;
import com.mega._NY.cart.dto.CartDTO;
import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.Cart.CartBuilder;
import com.mega._NY.cart.entity.ItemCart;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-27T14:13:04+0900",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Autowired
    private ItemCartMapper itemCartMapper;

    @Override
    public CartDTO toDTO(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartDTO cartDTO = new CartDTO();

        cartDTO.setUserId( cartUserId( cart ) );
        cartDTO.setItemCarts( itemCartListToItemCartDTOList( cart.getItemCarts() ) );
        cartDTO.setCartId( cart.getCartId() );
        cartDTO.setTotalItems( cart.getTotalItems() );
        cartDTO.setTotalPrice( cart.getTotalPrice() );
        cartDTO.setTotalDiscountPrice( cart.getTotalDiscountPrice() );

        return cartDTO;
    }

    @Override
    public Cart toEntity(CartDTO cartDTO) {
        if ( cartDTO == null ) {
            return null;
        }

        CartBuilder cart = Cart.builder();

        cart.cartId( cartDTO.getCartId() );
        cart.userId( cartDTO.getUserId() );
        cart.totalItems( cartDTO.getTotalItems() );
        cart.totalPrice( cartDTO.getTotalPrice() );
        cart.totalDiscountPrice( cartDTO.getTotalDiscountPrice() );

        return cart.build();
    }

    private Long cartUserId(Cart cart) {
        if ( cart == null ) {
            return null;
        }
        User user = cart.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<ItemCartDTO> itemCartListToItemCartDTOList(List<ItemCart> list) {
        if ( list == null ) {
            return null;
        }

        List<ItemCartDTO> list1 = new ArrayList<ItemCartDTO>( list.size() );
        for ( ItemCart itemCart : list ) {
            list1.add( itemCartMapper.toDTO( itemCart ) );
        }

        return list1;
    }
}
