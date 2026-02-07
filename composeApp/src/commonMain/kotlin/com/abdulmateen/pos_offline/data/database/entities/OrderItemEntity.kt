package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

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
    val productName: String,
    val imagePath: String? = null,
    val sku: String,
    val quantity: Double,
    val price: Double,
    val discount: Double
)