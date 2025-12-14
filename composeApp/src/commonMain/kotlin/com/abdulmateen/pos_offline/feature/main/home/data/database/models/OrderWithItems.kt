package com.abdulmateen.pos_offline.feature.main.home.data.database.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents a complete order, including the main order details and all associated items.
 *
 * This data class is used by Room to query an [OrderEntity] and its corresponding list
 * of [OrderItemEntity] objects in a single database operation.
 *
 * @property order The main order entity, embedded within this object.
 * @property items The list of items associated with this order. The relationship is
 *                 defined by matching the `orderId` in [OrderEntity] (parent) with the
 *                 `orderId` in [OrderItemEntity] (child).
 */
data class OrderWithItems(
    @Embedded
    val order: OrderEntity,

    @Relation(
        entity = OrderItemEntity::class,
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
)
