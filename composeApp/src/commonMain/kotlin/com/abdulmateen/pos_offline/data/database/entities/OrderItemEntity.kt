package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.abdulmateen.pos_offline.data.database.entities.ProductEntity

@Entity(
    tableName = "order_items",
    primaryKeys = ["orderId", "productId"],
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.Companion.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.Companion.RESTRICT
        )
    ],
    indices = [Index("productId")]
)
data class OrderItemEntity(
    val orderId: Long,
    val productId: Long,
    val quantity: Double,
    val unitPrice: Double
)