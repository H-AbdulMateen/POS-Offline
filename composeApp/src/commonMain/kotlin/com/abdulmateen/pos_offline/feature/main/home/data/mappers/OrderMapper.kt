package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.feature.main.home.data.database.models.OrderEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Order

fun OrderEntity.toOrder(): Order {
    return Order(
        orderId = orderId,
        totalAmount = totalAmount,
        createdAt = createdAt
    )
}

fun Order.toOrderEntity(): OrderEntity {
    return OrderEntity(
        totalAmount = totalAmount
    )
}