package com.abdulmateen.pos_offline.feature.customer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import com.abdulmateen.pos_offline.domain.repository.CustomerRepository
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import com.abdulmateen.pos_offline.feature.home.presentation.utils.shareFile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CustomerLedgerViewModel(
    private val customerId: Long,
    private val repository: CustomerRepository,
    private val orderRepository: OrderRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerLedgerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLedger()
        observeDataStore()
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            val businessName = dataStoreManager.getStringValue(PrefKeys.BUSINESS_NAME)
            val currencySymbol = dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL).ifEmpty { "$" }
            _uiState.update { it.copy(businessName = businessName, currencySymbol = currencySymbol) }
        }
    }


    private fun loadLedger() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val customer = repository.getCustomerById(customerId)
            
            combine(
                repository.getCustomerOrders(customerId),
                repository.getCustomerCredits(customerId)
            ) { orders, credits ->
                val transactions = mutableListOf<LedgerTransaction>()
                
                orders.forEach { order ->
                    transactions.add(
                        LedgerTransaction(
                            id = order.orderId,
                            date = order.createdAt,
                            type = "Order",
                            amount = order.total,
                            details = "Order #${order.orderId}"
                        )
                    )
                }

                credits.forEach { credit ->
                    if (credit.remainingAmount > 0) {
                        transactions.add(
                            LedgerTransaction(
                                id = credit.creditId,
                                date = credit.date,
                                type = "Credit",
                                amount = credit.remainingAmount,
                                details = "Credit Transaction"
                            )
                        )
                    }
                }

                val sortedTransactions = transactions.sortedByDescending { it.date }
                val totalBalance = credits.sumOf { it.remainingAmount }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        customerName = customer?.name ?: "",
                        transactions = sortedTransactions,
                        totalOutstandingBalance = totalBalance
                    )
                }
            }.collect()
        }
    }

    fun onTransactionClick(transaction: LedgerTransaction) {
        if (transaction.type == "Order") {
            viewModelScope.launch {
                orderRepository.getOrderWithItems(transaction.id).collect { orderWithItems ->
                    _uiState.update {
                        it.copy(
                            selectedOrder = orderWithItems,
                            showOrderDetails = true
                        )
                    }
                }
            }
        }
    }

    fun dismissOrderDetails() {
        _uiState.update { it.copy(showOrderDetails = false, selectedOrder = null) }
    }

    fun exportToCsv() {
        val transactions = uiState.value.transactions
        val customerName = uiState.value.customerName
        
        val csvHeader = "Date,Type,Details,Amount\n"
        val csvContent = transactions.joinToString("\n") { transaction ->
            val date = Instant.fromEpochMilliseconds(transaction.date)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val dateStr = "${date.dayOfMonth}/${date.monthNumber}/${date.year}"
            "$dateStr,${transaction.type},${transaction.details},${transaction.amount}"
        }
        
        val fullCsv = csvHeader + csvContent
        val fileName = "${customerName.replace(" ", "_")}_Ledger.csv"
        
        shareFile(fullCsv.encodeToByteArray(), fileName, "text/csv")
    }
}
