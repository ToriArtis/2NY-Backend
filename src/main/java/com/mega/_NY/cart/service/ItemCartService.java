package com.mega._NY.cart.service;

import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.cart.dto.ItemCartDTO;
import com.mega._NY.cart.entity.Cart;
import com.mega._NY.cart.entity.ItemCart;
import com.mega._NY.cart.mapper.ItemCartMapper;
import com.mega._NY.cart.repository.ItemCartRepository;
import com.mega._NY.cart.util.EntityUtils;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.Orders;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemCartService {

    private final ItemCartRepository itemCartRepository;
    private final ItemCartMapper itemCartMapper;
    private final ItemRepository itemRepository;

    // 장바구니에 상품 추가
    public ItemCartDTO addItemCart(ItemCartDTO itemCartDTO, Long itemId, Cart cart) {
        Item item = EntityUtils.findVerifiedEntity(itemRepository, itemId, ExceptionCode.ITEMCART_NOT_FOUND);

        ItemCart existingItemCart = itemCartRepository.findByCartAndItem(cart, item);
        if(existingItemCart == null) {
            ItemCart newItemCart = itemCartMapper.toEntity(itemCartDTO);
            newItemCart.setCart(cart);
            newItemCart.setItem(item);
            newItemCart.setBuyNow(true);
            return itemCartMapper.toDTO(itemCartRepository.save(newItemCart));
        } else {
            existingItemCart.addQuantity(itemCartDTO.getQuantity());
            return itemCartMapper.toDTO(itemCartRepository.save(existingItemCart));
        }
    }

    // 장바구니 상품 수량 변경
    public ItemCartDTO updownItemCart(long itemCartId, int upDown) {
        ItemCart itemCart = findVerifiedItemCart(itemCartId);
        itemCart.addQuantity(upDown);
        return itemCartMapper.toDTO(itemCartRepository.save(itemCart));
    }

    // 장바구니 상품 구매 여부 변경
    public ItemCartDTO excludeItemCart(long itemCartId, boolean buyNow) {
        ItemCart itemCart = findVerifiedItemCart(itemCartId);
        itemCart.setBuyNow(buyNow);
        return itemCartMapper.toDTO(itemCartRepository.save(itemCart));
    }

    // 장바구니에서 상품 삭제
    public long deleteItemCart(long itemCartId) {
        ItemCart itemCart = findVerifiedItemCart(itemCartId);
        long cartId = itemCart.getCart().getCartId();
        itemCartRepository.delete(itemCart);
        return cartId;
    }

    public List<ItemCartDTO> findItemCarts(Cart cart, Boolean buyNow) {
        List<ItemCart> itemCarts = buyNow == null ?
                itemCartRepository.findAllByCart(cart) :
                itemCartRepository.findAllByCartAndBuyNow(cart, buyNow);
        return itemCarts.stream()
                .map(itemCartMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void updateCartTotals(Cart cart) {
        List<ItemCartDTO> itemCarts = findItemCarts(cart, null);
        int totalPrice = calculateTotalPrice(itemCarts);
        int totalDiscountPrice = calculateTotalDiscountPrice(itemCarts);
        int totalItems = itemCarts.size();

        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountPrice(totalDiscountPrice);
        cart.setTotalItems(totalItems);
    }

    private int calculateTotalPrice(List<ItemCartDTO> itemCarts) {
        if(itemCarts == null) return 0;

        int totalPrice = 0;

        for(ItemCartDTO itemCart : itemCarts) {
            int quantity = itemCart.getQuantity();
            int price = itemCart.getPrice();
            totalPrice += (quantity * price);
        }

        return totalPrice;
    }

    private int calculateTotalDiscountPrice(List<ItemCartDTO> itemCarts) {
        if(itemCarts == null) return 0;

        int totalDiscountPrice = 0;

        for(ItemCartDTO itemCart : itemCarts) {
            int quantity = itemCart.getQuantity();
            int price = itemCart.getPrice();
            int discountRate = itemCart.getDiscountRate();

            totalDiscountPrice += (quantity * price * discountRate/100);
        }

        return totalDiscountPrice;

    }

    // 장바구니에 담긴 상품 수
    private int countTotalItems(List<ItemCartDTO> itemCarts) {
        return itemCarts.size();
    }

    private ItemCart findVerifiedItemCart(long itemCartId) {
        return EntityUtils.findVerifiedEntity(itemCartRepository, itemCartId, ExceptionCode.ITEMCART_NOT_FOUND);
    }

    // 장바구니의 모든 아이템 제거
    public void removeAllItemCartsFromCart(Cart cart) {
        List<ItemCart> itemCarts = itemCartRepository.findAllByCart(cart);
        itemCartRepository.deleteAll(itemCarts);
    }

    public ItemOrders createItemOrderFromCartItem(ItemCart itemCart, Orders order) {
        ItemOrders itemOrder = new ItemOrders();
        itemOrder.setItem(itemCart.getItem());
        itemOrder.setQuantity(itemCart.getQuantity());
        itemOrder.setOrders(order);
        itemOrder.setPrice(itemCart.getItem().getPrice());  // Item의 가격 정보 설정
        return itemOrder;
    }
}
