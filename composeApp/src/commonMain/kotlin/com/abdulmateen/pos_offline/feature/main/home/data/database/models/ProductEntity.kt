package com.abdulmateen.pos_offline.feature.main.home.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = UnitEntity::class,
            parentColumns = ["unitId"],
            childColumns = ["unitId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("categoryId"),
        Index("unitId"),
        Index(value = ["sku"], unique = true),
        Index(value = ["barcode"], unique = true),
        Index(value = ["name"])
    ]
)
data class ProductEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val productId: Long = 0,
    val name: String,
    val description: String? = null,
    val sku: String,
    val barcode: String,
    val purchasePrice: Double,
    val salePrice: Double,
    val quantity: Double,
    val imagePath: String? = null,
    val categoryId: Long? = null,
    val unitId: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
