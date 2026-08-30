package com.abdulmateen.pos_offline.data.mappers

import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import com.abdulmateen.pos_offline.domain.models.Order

fun OrderEntity.toOrder(): Order {
    return Order(
        orderId = orderId,
        totalAmount = total,
        customerName = customerName,
        customerPhone = customerPhone,
        subTotal = subTotal,
        discount = discount,
        tax = tax,
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus,
        total = total,
        createdAt = createdAt
    )
}

fun Order.toOrderEntity(): OrderEntity {
    return OrderEntity(
        total = totalAmount,
        customerName = customerName,
        customerPhone = customerPhone,
        subTotal = subTotal,
        discount = discount,
        tax = tax,
        paymentMethod = paymentMethod,
        paymentStatus = paymentStatus
    )
}
