package com.abdulmateen.pos_offline.data.database

import androidx.room.TypeConverter
import com.abdulmateen.pos_offline.data.database.entities.ExpenseCategory

object ExpenseTypeConverter {
    @TypeConverter
    fun fromExpenseCategory(category: ExpenseCategory): String {
        return category.name
    }

    @TypeConverter
    fun toExpenseCategory(category: String): ExpenseCategory {
        return ExpenseCategory.valueOf(category)
    }
}
