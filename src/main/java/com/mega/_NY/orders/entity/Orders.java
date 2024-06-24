package com.mega._NY.orders.entity;

import com.mega._NY.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "ORDERS")
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

//    @Column(nullable = false)
//    private String detailAddress;

    @Column(nullable = false)
    private String phone;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    // 주문 총 금액
    private int totalPrice;

    // 주문에 포함된 상품 목록
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<ItemOrders> itemOrders = new ArrayList<>();

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;


}
