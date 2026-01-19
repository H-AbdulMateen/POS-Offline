package com.abdulmateen.pos_offline.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

data class Cart(
    val cartId: Long = 0,
    val subTotal: Double = 0.0,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),

)
