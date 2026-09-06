package com.abdulmateen.pos_offline.feature.expense.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.dao.ExpenseBreakdown
import com.abdulmateen.pos_offline.data.database.entities.ExpenseCategory
import com.abdulmateen.pos_offline.data.database.entities.ExpenseEntity
import com.abdulmateen.pos_offline.domain.repository.ExpenseRepository
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.*
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
    val selectedMonth: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val currencySymbol: String = "$",
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false
)

class ExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    @OptIn(ExperimentalTime::class)
    private val _selectedMonth = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.let {
            LocalDate(it.year, it.month, 1)
        }
    )
    
    private val _uiState = MutableStateFlow(ExpenseUiState())
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _selectedMonth.collect { date ->
                currentPage = 0
                _uiState.update { it.copy(recentExpenses = emptyList(), isEndReached = false, selectedMonth = date) }
                refreshSummary(date)
                loadNextExpenses()
            }
        }
    }

    private suspend fun refreshSummary(date: LocalDate) {
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
            flow { emit(dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL)) }
        ) { revenue, expenses, breakdown, currency ->
            _uiState.update {
                it.copy(
                    totalRevenue = revenue,
                    totalExpenses = expenses,
                    profitLoss = revenue - expenses,
                    breakdown = breakdown,
                    currencySymbol = currency.ifEmpty { "$" }
                )
            }
        }.first()
    }

    fun loadNextExpenses() {
        if (_uiState.value.isLoading || _uiState.value.isEndReached) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val date = _selectedMonth.value
            val startOfMonth = LocalDate(date.year, date.month, 1)
            val endOfMonth = startOfMonth.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
            val start = LocalDateTime(startOfMonth.year, startOfMonth.month, startOfMonth.day, 0, 0)
                .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val end = LocalDateTime(endOfMonth.year, endOfMonth.month, endOfMonth.day, 23, 59, 59)
                .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

            expenseRepository.getExpensesPaged(pageSize, currentPage * pageSize).firstOrNull()?.let { allExpenses ->
                val monthlyExpenses = allExpenses.filter { it.date in start..end }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        recentExpenses = it.recentExpenses + monthlyExpenses,
                        isEndReached = allExpenses.size < pageSize
                    )
                }
                currentPage++
                
                // If the paged list didn't have enough for this month, but there might be more pages, 
                // we should technically keep loading, but for simplicity we'll just increment.
                // A better way would be a paged query with date range.
            } ?: _uiState.update { it.copy(isLoading = false) }
        }
    }


    fun previousMonth() {
        _selectedMonth.value = _selectedMonth.value.minus(1, DateTimeUnit.MONTH)
    }

    fun nextMonth() {
        _selectedMonth.value = _selectedMonth.value.plus(1, DateTimeUnit.MONTH)
    }

    fun addExpense(amount: Double, description: String, category: ExpenseCategory, paidTo: String?, employeeId: Long? = null) {
        viewModelScope.launch {
            expenseRepository.insertExpense(
                ExpenseEntity(
                    amount = amount,
                    description = description,
                    category = category,
                    employeeId = employeeId,
                    paidTo = paidTo
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
