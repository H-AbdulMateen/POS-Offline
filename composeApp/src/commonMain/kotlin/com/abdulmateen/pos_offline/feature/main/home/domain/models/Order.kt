package com.abdulmateen.pos_offline.feature.main.home.domain.models

import kotlin.time.Clock

data class Order(
    val orderId: Long = 0,
    val totalAmount: Double,
    val createdAt: Long
)
