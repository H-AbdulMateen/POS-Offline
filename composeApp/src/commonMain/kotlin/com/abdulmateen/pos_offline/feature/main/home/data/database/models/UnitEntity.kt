package com.abdulmateen.pos_offline.feature.main.home.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "units")
data class UnitEntity(
    @PrimaryKey(autoGenerate = true)
    val unitId: Long = 0,
    val name: String,           // e.g. "Piece", "Kilogram"
    val symbol: String          // e.g. "pcs", "kg"
)