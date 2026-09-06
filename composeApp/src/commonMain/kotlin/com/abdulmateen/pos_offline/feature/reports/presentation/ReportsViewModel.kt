package com.abdulmateen.pos_offline.feature.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock

data class ReportsUiState(
    val isLoading: Boolean = false,
    val exportMessage: String? = null,
    val isError: Boolean = false,
    val startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.minus(30, DateTimeUnit.DAY),
    val endDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
)

class ReportsViewModel(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    fun updateStartDate(date: LocalDate) {
        _uiState.update { 
            var newEndDate = it.endDate
            if (date > newEndDate) {
                newEndDate = date
            } else if (newEndDate > date.plus(30, DateTimeUnit.DAY)) {
                newEndDate = date.plus(30, DateTimeUnit.DAY)
            }
            it.copy(startDate = date, endDate = newEndDate) 
        }
    }

    fun updateEndDate(date: LocalDate) {
        _uiState.update { 
            var newStartDate = it.startDate
            if (date < newStartDate) {
                newStartDate = date
            } else if (newStartDate < date.minus(30, DateTimeUnit.DAY)) {
                newStartDate = date.minus(30, DateTimeUnit.DAY)
            }
            it.copy(startDate = newStartDate, endDate = date) 
        }
    }

    fun exportSales() {
        val state = _uiState.value
        val startMillis = state.startDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val endMillis = state.endDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds() + 86399999 // end of day

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, exportMessage = null) }
            val path = reportRepository.exportSalesReport(startMillis, endMillis)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    exportMessage = if (path != null) "Sales report exported to: $path" else "Failed to export sales report",
                    isError = path == null
                )
            }
        }
    }

    fun exportExpenses() {
        val state = _uiState.value
        val startMillis = state.startDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val endMillis = state.endDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds() + 86399999 // end of day

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, exportMessage = null) }
            val path = reportRepository.exportExpenseReport(startMillis, endMillis)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    exportMessage = if (path != null) "Expense report exported to: $path" else "Failed to export expense report",
                    isError = path == null
                )
            }
        }
    }
    
    fun clearMessage() {
        _uiState.update { it.copy(exportMessage = null) }
    }
}
