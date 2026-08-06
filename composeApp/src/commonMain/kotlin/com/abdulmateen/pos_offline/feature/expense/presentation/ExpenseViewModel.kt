package com.abdulmateen.pos_offline.feature.expense.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.dao.ExpenseBreakdown
import com.abdulmateen.pos_offline.data.database.entities.ExpenseCategory
import com.abdulmateen.pos_offline.data.database.entities.ExpenseEntity
import com.abdulmateen.pos_offline.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class ExpenseUiState @OptIn(ExperimentalTime::class) constructor(
    val totalRevenue: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val profitLoss: Double = 0.0,
    val breakdown: List<ExpenseBreakdown> = emptyList(),
    val recentExpenses: List<ExpenseEntity> = emptyList(),
    val selectedMonth: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
)

class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    @OptIn(ExperimentalTime::class)
    private val _selectedMonth = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.let {
            LocalDate(it.year, it.month, 1)
        }
    )
    
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ExpenseUiState> = _selectedMonth.flatMapLatest { date ->
        val startOfMonth = LocalDate(date.year, date.month, 1)
        val endOfMonth = startOfMonth.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
        
        val start = LocalDateTime(startOfMonth.year, startOfMonth.month, startOfMonth.day, 0, 0)
            .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val end = LocalDateTime(endOfMonth.year, endOfMonth.month, endOfMonth.day, 23, 59, 59)
            .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        
        combine(
            expenseRepository.getTotalRevenueInRange(start, end),
            expenseRepository.getTotalExpensesInRange(start, end),
            expenseRepository.getExpenseBreakdownInRange(start, end),
            expenseRepository.getAllExpenses()
        ) { revenue, expenses, breakdown, allExpenses ->
            ExpenseUiState(
                totalRevenue = revenue,
                totalExpenses = expenses,
                profitLoss = revenue - expenses,
                breakdown = breakdown,
                recentExpenses = allExpenses.filter { it.date in start..end },
                selectedMonth = startOfMonth
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseUiState())

    fun selectMonth(date: LocalDate) {
        _selectedMonth.value = LocalDate(date.year, date.month, 1)
    }

    fun addExpense(amount: Double, description: String, category: ExpenseCategory, employeeId: Long? = null) {
        viewModelScope.launch {
            expenseRepository.insertExpense(
                ExpenseEntity(
                    amount = amount,
                    description = description,
                    category = category,
                    employeeId = employeeId
                )
            )
        }
    }

    fun addRevenue(amount: Double, description: String) {
        viewModelScope.launch {
            expenseRepository.insertOrder(
                com.abdulmateen.pos_offline.data.database.entities.OrderEntity(
                    customerName = description,
                    subTotal = amount,
                    total = amount,
                    paymentMethod = "Manual",
                    paymentStatus = "Paid",
                    discount = 0.0,
                    tax = 0.0
                )
            )
        }
    }
}
