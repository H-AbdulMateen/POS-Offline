package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class OrderItemWithProduct(
    @Embedded
    val orderItem: OrderItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ProductEntity
)
