package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.data.database.dao.EmployeeDao
import com.abdulmateen.pos_offline.data.database.dao.ExpenseBreakdown
import com.abdulmateen.pos_offline.data.database.dao.ExpenseDao
import com.abdulmateen.pos_offline.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.data.database.entities.EmployeeEntity
import com.abdulmateen.pos_offline.data.database.entities.ExpenseEntity
import com.abdulmateen.pos_offline.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao,
    private val employeeDao: EmployeeDao,
    private val orderDao: OrderDao
) : ExpenseRepository {
    override suspend fun insertExpense(expense: ExpenseEntity) = expenseDao.insertExpense(expense)
    override suspend fun updateExpense(expense: ExpenseEntity) = expenseDao.updateExpense(expense)
    override suspend fun deleteExpense(expense: ExpenseEntity) = expenseDao.deleteExpense(expense)
    override fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()
    override fun getTotalExpensesInRange(start: Long, end: Long): Flow<Double> = 
        expenseDao.getTotalExpensesInRange(start, end).map { it ?: 0.0 }
    override fun getExpenseBreakdownInRange(start: Long, end: Long): Flow<List<ExpenseBreakdown>> = 
        expenseDao.getExpenseBreakdownInRange(start, end)

    override suspend fun insertEmployee(employee: EmployeeEntity) = employeeDao.insertEmployee(employee)
    override suspend fun updateEmployee(employee: EmployeeEntity) = employeeDao.updateEmployee(employee)
    override suspend fun deleteEmployee(employee: EmployeeEntity) = employeeDao.deleteEmployee(employee)
    override fun getAllEmployees(): Flow<List<EmployeeEntity>> = employeeDao.getAllEmployees()

    override fun getTotalRevenueInRange(start: Long, end: Long): Flow<Double> = 
        orderDao.getTotalRevenueInRange(start, end).map { it ?: 0.0 }

    override suspend fun insertOrder(order: com.abdulmateen.pos_offline.data.database.entities.OrderEntity) {
        orderDao.insertOrUpdateOrder(order)
    }
}
