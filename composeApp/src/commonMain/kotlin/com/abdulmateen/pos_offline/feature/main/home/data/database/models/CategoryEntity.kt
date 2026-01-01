package com.abdulmateen.pos_offline.feature.main.home.data.database.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "categories",
    indices = [Index(value = ["name"], unique = true)])
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long = 0,
    val name: String,
    val description: String? = null
)