package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "customers")
@OptIn(ExperimentalTime::class)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val customerId: Long = 0,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
