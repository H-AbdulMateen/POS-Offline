package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val cartId: Long = 0,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
)
