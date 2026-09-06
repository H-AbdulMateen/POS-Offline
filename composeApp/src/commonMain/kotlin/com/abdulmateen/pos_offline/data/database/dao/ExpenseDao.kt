package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.abdulmateen.pos_offline.data.database.entities.ExpenseCategory
import com.abdulmateen.pos_offline.data.database.entities.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getExpensesPaged(limit: Int, offset: Int): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date >= :start AND date <= :end ORDER BY date DESC")
    fun getExpensesInRange(start: Long, end: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :start AND date <= :end")
    fun getTotalExpensesInRange(start: Long, end: Long): Flow<Double?>

    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE date >= :start AND date <= :end GROUP BY category")
    fun getExpenseBreakdownInRange(start: Long, end: Long): Flow<List<ExpenseBreakdown>>
}

data class ExpenseBreakdown(
    val category: ExpenseCategory,
    val total: Double
)
