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

    public void updateBuyNowStatus(Long itemOrderId, boolean buyNow) {
        ItemOrders itemOrder = itemOrderRepository.findById(itemOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemOrder ID"));

        // 이미 처리된 주문인지 확인
        if (itemOrder.isBuyNow() == buyNow) {
            return; // 이미 같은 상태라면 아무 작업도 하지 않음
        }

        itemOrder.setBuyNow(buyNow);
        itemOrderRepository.save(itemOrder);

        if (buyNow) {
            updateItemSales(itemOrder, true);
        } else {
            // buyNow가 false로 변경되는 경우 (주문 취소 등) sales를 감소시킬 수 있음
            updateItemSales(itemOrder, false);
        }
    }

    private void updateItemSales(ItemOrders itemOrder, boolean increase) {
        Item item = itemOrder.getItem();
        if (increase) {
            item.setSales(item.getSales() + itemOrder.getQuantity());
        } else {
            item.setSales(Math.max(0, item.getSales() - itemOrder.getQuantity()));
        }
        itemRepository.save(item);
    }

    // 사용자가 아이템을 구매했는지 확인
    public boolean hasUserPurchasedItem(Long userId, Long itemId) {
        return itemOrderRepository.existsByItemItemIdAndOrdersUserIdAndBuyNowTrue(itemId, userId);
    }


}
