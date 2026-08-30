package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "orders")
@OptIn(ExperimentalTime::class)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,
    val customerName: String?,
    val customerPhone: String? = null,
    val subTotal: Double,
    val discount: Double?,
    val tax: Double?,
    val total: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)