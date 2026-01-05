package com.abdulmateen.pos_offline.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object StringListTypeConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.Default.decodeFromString(value)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.Default.encodeToString(list)
    }
}