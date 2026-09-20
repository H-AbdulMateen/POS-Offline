package com.abdulmateen.pos_offline.feature.credit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderWithItems
import com.abdulmateen.pos_offline.domain.repository.CreditRepository
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import com.abdulmateen.pos_offline.feature.home.presentation.utils.shareFile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System

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
                    lastUpdated = System.now().toEpochMilliseconds()
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

    fun exportToCsv() {
        val credits = uiState.value.credits
        val customerName = customerName
        
        val csvHeader = "Date,Days,Total,Paid,Balance\n"
        val csvContent = credits.joinToString("\n") { credit ->
            val date = Instant.fromEpochMilliseconds(credit.date)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val dateStr = "${date.day}-${date.month.number}-${date.year}"
            val now = System.now().toEpochMilliseconds()
            val days = ((now - credit.date) / (1000L * 60L * 60L * 24L)).toInt()
            
            "$dateStr,$days,${credit.totalAmount},${credit.paidAmount},${credit.remainingAmount}"
        }
        
        val fullCsv = csvHeader + csvContent
        val fileName = "${customerName.replace(" ", "_")}_Credits.csv"
        shareFile(fullCsv.encodeToByteArray(), fileName, "text/csv")
    }
}
