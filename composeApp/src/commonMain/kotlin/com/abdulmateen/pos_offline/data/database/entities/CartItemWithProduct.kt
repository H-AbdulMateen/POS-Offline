package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CartItemWithProduct(
    @Embedded
    val cartItem: CartItemEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ProductEntity
)
