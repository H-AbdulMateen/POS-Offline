package com.abdulmateen.pos_offline.feature.credit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.domain.repository.CreditRepository
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class CreditUiState(
    val credits: List<CreditEntity> = emptyList(),
    val currencySymbol: String = "$",
    val isLoading: Boolean = false
)

class CreditViewModel(
    private val creditRepository: CreditRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CreditUiState> = _searchQuery.flatMapLatest { query ->
        val creditsFlow = if (query.isEmpty()) {
            creditRepository.getAllCredits()
        } else {
            creditRepository.searchCredits(query)
        }
        
        combine(
            creditsFlow,
            flow { emit(dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL)) }
        ) { credits, symbol ->
            CreditUiState(
                credits = credits,
                currencySymbol = symbol.ifEmpty { "$" }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreditUiState())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(ExperimentalTime::class)
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

    @OptIn(ExperimentalTime::class)
    fun addCredit(customerName: String, phoneNumber: String?, totalAmount: Double, paidAmount: Double) {
        viewModelScope.launch {
            creditRepository.upsertCredit(
                CreditEntity(
                    customerName = customerName,
                    phoneNumber = phoneNumber,
                    totalAmount = totalAmount,
                    paidAmount = paidAmount,
                    remainingAmount = totalAmount - paidAmount,
                    date = Clock.System.now().toEpochMilliseconds(),
                    lastUpdated = Clock.System.now().toEpochMilliseconds()
                )
            )
        }
    }

    fun deleteCredit(credit: CreditEntity) {
        viewModelScope.launch {
            creditRepository.deleteCredit(credit)
        }
    }
}
