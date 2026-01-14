package com.abdulmateen.pos_offline.data.mappers

import com.abdulmateen.pos_offline.data.database.entities.CartEntity
import com.abdulmateen.pos_offline.data.database.entities.CartItemEntity
import com.abdulmateen.pos_offline.domain.models.Cart
import com.abdulmateen.pos_offline.domain.models.CartItem

fun CartEntity.toCart(): Cart {
    return Cart(
        cartId = cartId,
        createdAt = createdAt
    )
}

fun Cart.toCartEntity(): CartEntity {
    return CartEntity(
        cartId = cartId,
        createdAt = createdAt
    )
}

fun CartItem.toCartItemEntity(): CartItemEntity{
    return CartItemEntity(
        cartItemId = cartItemId,
        productId = productId,
        productName = productName,
        cartId = cartId,
        sku = sku,
        quantity = quantity,
        price = price,
        discount = discount
    )
}

fun CartItemEntity.toCartItem(): CartItem {
    return CartItem(
        cartItemId = cartItemId,
        cartId = cartId,
        productId = productId,
        productName = productName,
        sku = sku,
        quantity = quantity,
        price = price,
        discount = discount
    )
}