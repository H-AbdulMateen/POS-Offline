package com.abdulmateen.pos_offline.feature.credit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.domain.repository.CreditRepository
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import com.abdulmateen.pos_offline.feature.home.presentation.utils.shareFile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System

data class CreditSummary(
    val customerName: String,
    val phoneNumber: String?,
    val totalAmount: Double,
    val paidAmount: Double,
    val remainingAmount: Double,
    val oldestDate: Long,
    val noOfDays: Int
)

data class CreditUiState(
    val summaries: List<CreditSummary> = emptyList(),
    val currencySymbol: String = "$",
    val isLoading: Boolean = false
)

class CreditViewModel(
    private val creditRepository: CreditRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow(CreditUiState())
    val uiState: StateFlow<CreditUiState> = _uiState.asStateFlow()

    init {
        loadSummaries()
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
                loadSummaries(query)
            }
        }
    }

    private fun loadSummaries(query: String = "") {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val baseFlow = if (query.isEmpty()) {
                creditRepository.getAllCredits()
            } else {
                creditRepository.searchCredits(query)
            }
            
            baseFlow.collect { allCredits ->
                val summaries = allCredits.groupBy { it.customerName }.map { (name, list) ->
                    val total = list.sumOf { it.totalAmount }
                    val paid = list.sumOf { it.paidAmount }
                    val remaining = list.sumOf { it.remainingAmount }
                    val oldest = list.minOf { it.date }
                    CreditSummary(
                        customerName = name,
                        phoneNumber = list.firstOrNull { !it.phoneNumber.isNullOrBlank() }?.phoneNumber,
                        totalAmount = total,
                        paidAmount = paid,
                        remainingAmount = remaining,
                        oldestDate = oldest,
                        noOfDays = calculateDays(oldest)
                    )
                }.filter { it.remainingAmount > 0.0 }
                .sortedByDescending { it.noOfDays }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        summaries = summaries
                    )
                }
            }
        }
    }

    private fun calculateDays(timestamp: Long): Int {
        val now = System.now().toEpochMilliseconds()
        val diff = now - timestamp
        val msPerDay = 86400000L
        return (diff / msPerDay).toInt()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun exportReport() {
        val csvHeader = "Customer,Phone,Oldest Date,Days,Total,Paid,Balance\n"
        val summaries = _uiState.value.summaries
        val csvContent = summaries.joinToString("\n") { summary ->
            val date = Instant.fromEpochMilliseconds(summary.oldestDate)
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val dateStr = "${date.dayOfMonth}-${date.monthNumber}-${date.year}"
            "${summary.customerName},${summary.phoneNumber ?: ""},$dateStr,${summary.noOfDays},${summary.totalAmount},${summary.paidAmount},${summary.remainingAmount}"
        }

        val fullCsv = csvHeader + csvContent
        val fileName = "Credit_Summary_${System.now().toEpochMilliseconds()}.csv"
        shareFile(fullCsv.encodeToByteArray(), fileName, "text/csv")
    }
}
