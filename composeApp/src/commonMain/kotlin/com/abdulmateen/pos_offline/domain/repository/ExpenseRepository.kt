package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.data.database.dao.ExpenseBreakdown
import com.abdulmateen.pos_offline.data.database.entities.EmployeeEntity
import com.abdulmateen.pos_offline.data.database.entities.ExpenseEntity
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun insertExpense(expense: ExpenseEntity)
    suspend fun updateExpense(expense: ExpenseEntity)
    suspend fun deleteExpense(expense: ExpenseEntity)
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    fun getExpensesPaged(limit: Int, offset: Int): Flow<List<ExpenseEntity>>
    fun getTotalExpensesInRange(start: Long, end: Long): Flow<Double>
    fun getExpenseBreakdownInRange(start: Long, end: Long): Flow<List<ExpenseBreakdown>>

    suspend fun insertEmployee(employee: EmployeeEntity)
    suspend fun updateEmployee(employee: EmployeeEntity)
    suspend fun deleteEmployee(employee: EmployeeEntity)
    fun getAllEmployees(): Flow<List<EmployeeEntity>>
    
    fun getTotalRevenueInRange(start: Long, end: Long): Flow<Double>

    suspend fun insertOrder(order: com.abdulmateen.pos_offline.data.database.entities.OrderEntity)
}
