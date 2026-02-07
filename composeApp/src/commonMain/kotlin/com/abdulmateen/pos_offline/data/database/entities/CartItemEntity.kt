package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = CartEntity::class,
            parentColumns = ["cartId"],
            childColumns = ["cartId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"]
        )
    ],
    indices = [
        Index("cartId"),
        Index("productId"),
        Index(value = ["cartId", "productId"], unique = true)
    ]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val cartItemId: Long = 0,
    val cartId: Long,
    val productId: Long,
    val productName: String,
    val imagePath: String? = null,
    val sku: String,
    val quantity: Double,
    val price: Double,        // snapshot price
    val discount: Double = 0.0
)
