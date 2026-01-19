package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CartWithItemsViewTable(
    @Embedded val cart: CartEntity,

    @Relation(
        entity = CartItemEntity::class,
        parentColumn = "cartId",
        entityColumn = "cartId"
    )
    val items: List<CartItemWithProduct>
)
