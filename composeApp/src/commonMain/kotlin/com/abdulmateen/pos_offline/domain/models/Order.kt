package com.abdulmateen.pos_offline.domain.models

data class Order(
    val orderId: Long = 0,
    val totalAmount: Double,
    val createdAt: Long
)