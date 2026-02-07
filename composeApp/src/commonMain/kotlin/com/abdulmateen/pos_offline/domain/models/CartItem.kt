package com.abdulmateen.pos_offline.domain.models

data class CartItem(
    val cartItemId: Long = 0,
    val cartId: Long = 0,
    val productId: Long,
    val productName: String,
    val imagePath: String? = null,
    val photoBytes: ByteArray? = null,
    val sku: String,
    val quantity: Double,
    val unitPrice: Double,
    val price: Double = unitPrice * quantity,
    val discount: Double = 0.0
)
