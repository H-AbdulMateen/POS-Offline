package com.abdulmateen.pos_offline.feature.main.home.domain.models

data class OrderItem(
    val orderId: Long,
    val productId: Long,
    val quantity: Double,
    val unitPrice: Double
)