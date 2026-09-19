package com.abdulmateen.pos_offline.feature.customer.presentation

import com.abdulmateen.pos_offline.data.database.entities.OrderWithItems

data class CustomerLedgerUiState(
    val isLoading: Boolean = false,
    val customerName: String = "",
    val transactions: List<LedgerTransaction> = emptyList(),
    val totalOutstandingBalance: Double = 0.0,
    val selectedOrder: OrderWithItems? = null,
    val showOrderDetails: Boolean = false,
    val businessName: String = "",
    val currencySymbol: String = "$"
)

data class LedgerTransaction(
    val id: Long,
    val date: Long,
    val type: String,
    val amount: Double,
    val details: String
)
