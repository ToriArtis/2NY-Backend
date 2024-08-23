package com.mega._NY.cart.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCart is a Querydsl query type for Cart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCart extends EntityPathBase<Cart> {

    private static final long serialVersionUID = -70264464L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCart cart = new QCart("cart");

    public final NumberPath<Long> cartId = createNumber("cartId", Long.class);

    public final ListPath<ItemCart, QItemCart> itemCarts = this.<ItemCart, QItemCart>createList("itemCarts", ItemCart.class, QItemCart.class, PathInits.DIRECT2);

    public final NumberPath<Integer> totalDiscountPrice = createNumber("totalDiscountPrice", Integer.class);

    public final NumberPath<Integer> totalItems = createNumber("totalItems", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final com.mega._NY.auth.entity.QUser user;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCart(String variable) {
        this(Cart.class, forVariable(variable), INITS);
    }

    public QCart(Path<? extends Cart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCart(PathMetadata metadata, PathInits inits) {
        this(Cart.class, metadata, inits);
    }

    public QCart(Class<? extends Cart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.mega._NY.auth.entity.QUser(forProperty("user")) : null;
    }

}

