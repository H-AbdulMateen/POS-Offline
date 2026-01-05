package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.data.database.entities.OrderItemEntity
import com.abdulmateen.pos_offline.domain.models.OrderItem

fun OrderItemEntity.toOrderItem(): OrderItem {
    return OrderItem(
        orderId = orderId,
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice
    )
}

fun OrderItem.toOrderItemEntity() = OrderItemEntity(
    orderId = orderId,
    productId = productId,
    quantity = quantity,
    unitPrice = unitPrice
)
