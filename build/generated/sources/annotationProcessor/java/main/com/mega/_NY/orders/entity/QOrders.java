package com.mega._NY.orders.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrders is a Querydsl query type for Orders
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrders extends EntityPathBase<Orders> {

    private static final long serialVersionUID = 1921982330L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrders orders = new QOrders("orders");

    public final StringPath address = createString("address");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath detailAddress = createString("detailAddress");

    public final NumberPath<Integer> expectPrice = createNumber("expectPrice", Integer.class);

    public final ListPath<ItemOrders, QItemOrders> itemOrders = this.<ItemOrders, QItemOrders>createList("itemOrders", ItemOrders.class, QItemOrders.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final EnumPath<OrderStatus> orderStatus = createEnum("orderStatus", OrderStatus.class);

    public final StringPath phone = createString("phone");

    public final NumberPath<Integer> totalDiscountPrice = createNumber("totalDiscountPrice", Integer.class);

    public final NumberPath<Integer> totalItems = createNumber("totalItems", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final com.mega._NY.auth.entity.QUser user;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QOrders(String variable) {
        this(Orders.class, forVariable(variable), INITS);
    }

    public QOrders(Path<? extends Orders> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrders(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrders(PathMetadata metadata, PathInits inits) {
        this(Orders.class, metadata, inits);
    }

    public QOrders(Class<? extends Orders> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.mega._NY.auth.entity.QUser(forProperty("user")) : null;
    }

}

