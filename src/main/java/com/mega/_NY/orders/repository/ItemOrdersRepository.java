package com.mega._NY.orders.repository;

import com.mega._NY.orders.entity.ItemOrders;
import com.mega._NY.orders.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemOrdersRepository extends JpaRepository<ItemOrders, Long>{

    // 특정 사용자의 특정 상태의 주문 항목들을 페이지네이션하여 조회
    @Query("SELECT io FROM ITEM_ORDERS io JOIN io.orders o WHERE o.user.id = :userId AND o.orderStatus = :orderStatus")
    Page<ItemOrders> findAllByUserIdAndOrderStatus(@Param("userId") Long userId, @Param("orderStatus") OrderStatus orderStatus, Pageable pageable);

    List<ItemOrders> findByItemItemIdAndOrdersUserId(Long itemId, Long userId);
    boolean existsByItemItemIdAndOrdersUserIdAndBuyNowTrue(Long itemId, Long userId);

}
