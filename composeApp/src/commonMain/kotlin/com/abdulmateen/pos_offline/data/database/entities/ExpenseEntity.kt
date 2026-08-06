package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

enum class ExpenseCategory {
    SALARY,
    BILL,
    TRANSPORTATION,
    OTHER
}

@Entity(tableName = "expenses")
data class ExpenseEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Long = 0,
    val amount: Double,
    val description: String,
    val category: ExpenseCategory,
    val employeeId: Long? = null,
    val paidTo: String? = null,
    val date: Long = Clock.System.now().toEpochMilliseconds()
)
