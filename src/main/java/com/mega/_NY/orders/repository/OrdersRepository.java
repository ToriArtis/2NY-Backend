package com.mega._NY.orders.repository;

import com.mega._NY.orders.entity.OrderStatus;
import com.mega._NY.orders.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrdersRepository extends JpaRepository<Orders, Long> {

    // 특정 사용자의 모든 주문을 페이지네이션하여 조회
    Page<Orders> findAllByUserId(Long userId, Pageable pageable);

    @Override
    Page<Orders> findAll(Pageable pageable);

    // 특정 사용자의 특정 상태의 주문을 페이지네이션하여 조회
    Page<Orders> findAllByUserAndOrderStatus(Long userId, OrderStatus orderStatus, Pageable pageable);

}
