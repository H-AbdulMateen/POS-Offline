package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "returns")
@OptIn(ExperimentalTime::class)
data class ReturnEntity(
    @PrimaryKey(autoGenerate = true)
    val returnId: Long = 0,
    val orderId: Long,
    val totalReturnAmount: Double,
    val reason: String?,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)

@Entity(tableName = "return_items")
data class ReturnItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val returnId: Long,
    val productId: Long,
    val quantity: Double,
    val amount: Double
)
