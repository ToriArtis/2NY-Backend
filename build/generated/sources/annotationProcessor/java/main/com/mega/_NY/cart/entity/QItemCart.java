package com.mega._NY.cart.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemCart is a Querydsl query type for ItemCart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemCart extends EntityPathBase<ItemCart> {

    private static final long serialVersionUID = -1169072349L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemCart itemCart = new QItemCart("itemCart");

    public final BooleanPath buyNow = createBoolean("buyNow");

    public final QCart cart;

    public final com.mega._NY.item.entity.QItem item;

    public final NumberPath<Long> itemCartId = createNumber("itemCartId", Long.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QItemCart(String variable) {
        this(ItemCart.class, forVariable(variable), INITS);
    }

    public QItemCart(Path<? extends ItemCart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemCart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemCart(PathMetadata metadata, PathInits inits) {
        this(ItemCart.class, metadata, inits);
    }

    public QItemCart(Class<? extends ItemCart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new QCart(forProperty("cart"), inits.get("cart")) : null;
        this.item = inits.isInitialized("item") ? new com.mega._NY.item.entity.QItem(forProperty("item")) : null;
    }

}

