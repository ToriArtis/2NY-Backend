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

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String phone;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    @Setter
    private Integer totalItems; // 주문에 포함된 아이템 종류

    @Column
    @Setter
    private Integer totalPrice;

    @Column
    @Setter
    private Integer totalDiscountPrice;

    @Column
    @Setter
    private Integer expectPrice; // 실제 결제 금액 (정가 - 할인가)


    @Column(name = "userId")
    private Long userId;

    // User에 대한 단방향 관계 (옵션)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    // 주문에 포함된 상품 목록
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrders> itemOrders = new ArrayList<>();

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public void setUser(User user) {
        if (user != null) {
            this.userId = user.getId();
            this.user = user;
        }
    }

}
