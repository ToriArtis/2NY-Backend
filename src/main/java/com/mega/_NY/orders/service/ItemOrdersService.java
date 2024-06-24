package com.mega._NY.orders.service;

import com.mega._NY.auth.config.exception.BusinessLogicException;
import com.mega._NY.auth.config.exception.ExceptionCode;
import com.mega._NY.item.entity.Item;
import com.mega._NY.item.repository.ItemRepository;
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

    // 주문 항목들의 총 가격 계산
    public int calculateTotalPrice(List<ItemOrders> itemOrders) {
        return itemOrders.stream()
                .mapToInt(io -> io.getQuantity() * io.getItem().getPrice())
                .sum();
    }

    // 아이템 판매량 업데이트
    public void updateItemSales(ItemOrders itemOrder, boolean increase) {
        Item item = itemOrder.getItem();
        int salesChange = increase ? itemOrder.getQuantity() : -itemOrder.getQuantity();
        item.setSales(item.getSales() + salesChange);
        itemRepository.save(item);
    }

}
