package com.abdulmateen.pos_offline.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.dao.CategoryRevenue
import com.abdulmateen.pos_offline.data.database.dao.TopProduct
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import com.abdulmateen.pos_offline.domain.repository.DashboardRepository
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val repository: DashboardRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.getTotalRevenue(),
        repository.getTotalOrders(),
        repository.getTotalProducts(),
        repository.getTotalStock(),
        repository.getTopSellingProducts(),
        repository.getRevenueByCategory(),
        repository.getRecentOrders(),
        repository.getTotalReturns(),
        kotlinx.coroutines.flow.flow { emit(dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL)) }
    ) { array ->
        val revenue = array[0] as? Double ?: 0.0
        val returns = array[7] as? Double ?: 0.0
        val currencySymbol = (array[8] as? String)?.ifEmpty { "$" } ?: "$"
        DashboardUiState(
            totalRevenue = revenue - returns,
            totalOrders = array[1] as Int,
            totalProducts = array[2] as Int,
            totalStock = array[3] as? Double ?: 0.0,
            topSellingProducts = array[4] as List<TopProduct>,
            revenueByCategory = array[5] as List<CategoryRevenue>,
            recentOrders = array[6] as List<OrderEntity>,
            currencySymbol = currencySymbol,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )
}
