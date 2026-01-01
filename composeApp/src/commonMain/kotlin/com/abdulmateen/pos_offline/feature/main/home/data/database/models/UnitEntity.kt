package com.abdulmateen.pos_offline.feature.main.home.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "units",
    indices = [Index(value = ["name", "symbol"], unique = true)])
data class UnitEntity(
    @PrimaryKey(autoGenerate = true)
    val unitId: Long = 0,
    val name: String,           // e.g. "Piece", "Kilogram"
    val symbol: String          // e.g. "pcs", "kg"
)