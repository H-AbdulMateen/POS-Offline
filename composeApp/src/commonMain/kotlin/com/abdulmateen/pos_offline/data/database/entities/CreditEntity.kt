package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "credits")
data class CreditEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val creditId: Long = 0,
    val customerId: Long? = null,
    val orderId: Long? = null,
    val customerName: String,
    val phoneNumber: String? = null,
    val totalAmount: Double,
    val paidAmount: Double,
    val remainingAmount: Double,
    val date: Long = Clock.System.now().toEpochMilliseconds(),
    val lastUpdated: Long = Clock.System.now().toEpochMilliseconds()
)
