package com.mega._NY.orders.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemOrders is a Querydsl query type for ItemOrders
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemOrders extends EntityPathBase<ItemOrders> {

    private static final long serialVersionUID = -1863755283L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemOrders itemOrders = new QItemOrders("itemOrders");

    public final BooleanPath buyNow = createBoolean("buyNow");

    public final NumberPath<Integer> discountPrice = createNumber("discountPrice", Integer.class);

    public final com.mega._NY.item.entity.QItem item;

    public final NumberPath<Long> itemOrderId = createNumber("itemOrderId", Long.class);

    public final QOrders orders;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public QItemOrders(String variable) {
        this(ItemOrders.class, forVariable(variable), INITS);
    }

    public QItemOrders(Path<? extends ItemOrders> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemOrders(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemOrders(PathMetadata metadata, PathInits inits) {
        this(ItemOrders.class, metadata, inits);
    }

    public QItemOrders(Class<? extends ItemOrders> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new com.mega._NY.item.entity.QItem(forProperty("item")) : null;
        this.orders = inits.isInitialized("orders") ? new QOrders(forProperty("orders"), inits.get("orders")) : null;
    }

}

