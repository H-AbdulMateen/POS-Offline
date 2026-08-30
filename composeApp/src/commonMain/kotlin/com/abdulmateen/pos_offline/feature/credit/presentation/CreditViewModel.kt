package com.abdulmateen.pos_offline.feature.credit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.domain.repository.CreditRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class CreditUiState(
    val credits: List<CreditEntity> = emptyList(),
    val isLoading: Boolean = false
)

class CreditViewModel(
    private val creditRepository: CreditRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CreditUiState> = _searchQuery.flatMapLatest { query ->
        if (query.isEmpty()) {
            creditRepository.getAllCredits()
        } else {
            creditRepository.searchCredits(query)
        }
    }.flatMapLatest { credits ->
        MutableStateFlow(CreditUiState(credits = credits))
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
