package com.abdulmateen.pos_offline.domain.models

data class OrderItem(
    val orderId: Long,
    val productId: Long,
    val quantity: Double,
    val unitPrice: Double
)