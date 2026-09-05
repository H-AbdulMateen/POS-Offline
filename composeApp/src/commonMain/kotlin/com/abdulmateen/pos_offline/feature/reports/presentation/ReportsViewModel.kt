package com.abdulmateen.pos_offline.feature.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReportsUiState(
    val isLoading: Boolean = false,
    val exportMessage: String? = null,
    val isError: Boolean = false
)

class ReportsViewModel(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    fun exportSales() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, exportMessage = null) }
            val path = reportRepository.exportSalesReport()
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
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, exportMessage = null) }
            val path = reportRepository.exportExpenseReport()
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
