package com.mega._NY.orders.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
import com.mega._NY.orders.dto.ItemOrderDTO;
import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.repository.ItemOrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemOrdersService {

    private final ItemOrdersRepository itemOrderRepository;
    private final ItemRepository itemRepository;

    // 새로운 주문 항목 생성
    public ItemOrders createItemOrder(ItemOrders itemOrder) {
        return itemOrderRepository.save(itemOrder);
    }

    // ID로 주문 항목 조회
    public ItemOrders findItemOrder(long itemOrderId) {
        return itemOrderRepository.findById(itemOrderId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
    }

    // 주문 항목의 수량 업데이트
    public ItemOrders updateQuantity(long itemOrderId, int quantity) {
        ItemOrders itemOrder = findItemOrder(itemOrderId);
        itemOrder.setQuantity(quantity);
        return itemOrderRepository.save(itemOrder);
    }

    public int calculateTotalPrice( List<ItemOrders> itemOrders ){

        if(itemOrders == null) return 0;

        int totalPrice = 0;

        for(ItemOrders itemOrder : itemOrders){
            int quantity = itemOrder.getQuantity();
            int price = itemOrder.getItem().getPrice();
            totalPrice += ( quantity * price );
        }

        return totalPrice;
    }

    public int calculateDiscountTotalPrice( List<ItemOrders> itemOrders ){

        if(itemOrders == null) return 0;

        int totalDiscountPrice = 0;

        for(ItemOrders itemOrder : itemOrders){
            int quantity = itemOrder.getQuantity();
            int price = itemOrder.getItem().getPrice();
            int discountRate = itemOrder.getItem().getDiscountRate();
            totalDiscountPrice += ( quantity * price * discountRate / 100 );
        }

        return totalDiscountPrice;
    }

    // 아이템 판매량 업데이트
    public void updateItemSales(ItemOrders itemOrder, boolean increase) {
        Item item = itemOrder.getItem();
        int salesChange = increase ? itemOrder.getQuantity() : -itemOrder.getQuantity();
        item.setSales(item.getSales() + salesChange);
        itemRepository.save(item);
    }

    // 주문 완료 시 buyNow 상태 업데이트
    public void updateBuyNowStatus(Long itemId, Long userId, boolean buyNow) {
        List<ItemOrders> itemOrders = itemOrderRepository.findByItemItemIdAndOrdersUserId(itemId, userId);
        itemOrders.forEach(itemOrder -> {
            itemOrder.setBuyNow(buyNow);
            itemOrderRepository.save(itemOrder);
        });
    }

    // 사용자가 아이템을 구매했는지 확인
    public boolean hasUserPurchasedItem(Long userId, Long itemId) {
        return itemOrderRepository.existsByItemItemIdAndOrdersUserIdAndBuyNowTrue(itemId, userId);
    }


}
