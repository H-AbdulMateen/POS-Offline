package com.abdulmateen.pos_offline.feature.credit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderWithItems
import com.abdulmateen.pos_offline.domain.repository.CreditRepository
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Clock

data class CreditDetailUiState(
    val credits: List<CreditEntity> = emptyList(),
    val currencySymbol: String = "$",
    val businessName: String = "",
    val isLoading: Boolean = false,
    val selectedOrder: OrderWithItems? = null,
    val showOrderDetails: Boolean = false
)

class CreditDetailViewModel(
    private val customerName: String,
    private val creditRepository: CreditRepository,
    private val orderRepository: OrderRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreditDetailUiState())
    val uiState: StateFlow<CreditDetailUiState> = _uiState.asStateFlow()

    init {
        loadCredits()
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val symbol = dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL)
            val business = dataStoreManager.getStringValue(PrefKeys.BUSINESS_NAME)
            _uiState.update { it.copy(currencySymbol = symbol.ifEmpty { "$" }, businessName = business) }
        }
    }

    private fun loadCredits() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Since we don't have a specific "search by exact name" in repo, we use search and filter locally or use the existing flow
            creditRepository.getAllCredits().collect { all ->
                val filtered = all.filter { it.customerName == customerName }
                    .sortedByDescending { it.date }
                _uiState.update { it.copy(isLoading = false, credits = filtered) }
            }
        }
    }

    fun receivePayment(credit: CreditEntity, amount: Double) {
        viewModelScope.launch {
            val updatedPaidAmount = credit.paidAmount + amount
            val updatedRemainingAmount = (credit.totalAmount - updatedPaidAmount).coerceAtLeast(0.0)
            creditRepository.upsertCredit(
                credit.copy(
                    paidAmount = updatedPaidAmount,
                    remainingAmount = updatedRemainingAmount,
                    lastUpdated = Clock.System.now().toEpochMilliseconds()
                )
            )
        }
    }

    fun showOrderDetails(orderId: Long) {
        viewModelScope.launch {
            orderRepository.getOrderWithItems(orderId).collect { orderWithItems ->
                _uiState.update { 
                    it.copy(
                        selectedOrder = orderWithItems,
                        showOrderDetails = true
                    ) 
                }
            }
        }
    }

    fun dismissOrderDetails() {
        _uiState.update { it.copy(showOrderDetails = false, selectedOrder = null) }
    }
}
