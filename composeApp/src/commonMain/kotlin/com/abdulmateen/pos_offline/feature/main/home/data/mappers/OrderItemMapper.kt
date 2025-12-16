package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.feature.main.home.data.database.models.OrderItemEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.models.OrderItem

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
