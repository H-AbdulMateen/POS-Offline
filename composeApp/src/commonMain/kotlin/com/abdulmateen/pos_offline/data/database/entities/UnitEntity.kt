package com.abdulmateen.pos_offline.data.database.entities

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