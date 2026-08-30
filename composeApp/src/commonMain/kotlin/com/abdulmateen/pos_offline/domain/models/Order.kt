package com.abdulmateen.pos_offline.domain.models

import kotlin.time.Clock

data class Order(
    val orderId: Long = 0,
    val customerName: String?,
    val customerPhone: String? = null,
    val subTotal: Double,
    val discount: Double?,
    val tax: Double?,
    val total: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val totalAmount: Double,
    val createdAt: Long
)