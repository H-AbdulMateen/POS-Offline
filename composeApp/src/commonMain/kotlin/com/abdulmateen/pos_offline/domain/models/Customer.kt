package com.abdulmateen.pos_offline.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val customerId: Long = 0,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val createdAt: Long = 0
)
