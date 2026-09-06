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
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false
)

class CreditViewModel(
    private val creditRepository: CreditRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow(CreditUiState())
    val uiState: StateFlow<CreditUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20

    init {
        loadNextCredits()
        observeDataStore()
        observeSearch()
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            val symbol = dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL)
            _uiState.update { it.copy(currencySymbol = symbol.ifEmpty { "$" }) }
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery.debounce(300).collect { query ->
                if (query.isEmpty()) {
                    currentPage = 0
                    _uiState.update { it.copy(credits = emptyList(), isEndReached = false) }
                    loadNextCredits()
                } else {
                    creditRepository.searchCredits(query).collect { credits ->
                        _uiState.update { 
                            it.copy(
                                credits = credits,
                                isEndReached = true // Disable pagination during search
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadNextCredits() {
        if (_uiState.value.isLoading || _uiState.value.isEndReached || _searchQuery.value.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            creditRepository.getCreditsPaged(pageSize, currentPage * pageSize).firstOrNull()?.let { newCredits ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        credits = it.credits + newCredits,
                        isEndReached = newCredits.size < pageSize
                    )
                }
                currentPage++
            } ?: _uiState.update { it.copy(isLoading = false) }
        }
    }

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
