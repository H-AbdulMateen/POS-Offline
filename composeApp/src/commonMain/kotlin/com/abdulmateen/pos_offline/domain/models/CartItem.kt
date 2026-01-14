package com.abdulmateen.pos_offline.domain.models

data class CartItem(
    val cartItemId: Long = 0,
    val cartId: Long = 0,
    val productId: Long,
    val productName: String,
    val sku: String,
    val quantity: Double,
    val price: Double,
    val discount: Double = 0.0
)
