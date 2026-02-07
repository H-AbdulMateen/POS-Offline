package com.abdulmateen.pos_offline.data.mappers

import com.abdulmateen.pos_offline.core.data.filestorage.ImageStorage
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
        createdAt = createdAt
    )
}

fun CartItem.toCartItemEntity(): CartItemEntity{
    return CartItemEntity(
        cartItemId = cartItemId,
        productId = productId,
        productName = productName,
        imagePath = imagePath,
        cartId = cartId,
        sku = sku,
        quantity = quantity,
        price = unitPrice,
        discount = discount
    )
}

suspend fun CartItemEntity.toCartItem(imageStorage: ImageStorage): CartItem {
    return CartItem(
        cartItemId = cartItemId,
        cartId = cartId,
        productId = productId,
        productName = productName,
        sku = sku,
        quantity = quantity,
        unitPrice = price,
        discount = discount,
        price = price * quantity,
        photoBytes = this.imagePath?.let { imageStorage.getImage(it) }
    )
}

