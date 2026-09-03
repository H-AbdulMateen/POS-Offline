package com.abdulmateen.pos_offline.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.dao.CategoryRevenue
import com.abdulmateen.pos_offline.data.database.dao.TopProduct
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import com.abdulmateen.pos_offline.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.getTotalRevenue(),
        repository.getTotalOrders(),
        repository.getTotalProducts(),
        repository.getTotalStock(),
        repository.getTopSellingProducts(),
        repository.getRevenueByCategory(),
        repository.getRecentOrders(),
        repository.getTotalReturns()
    ) { array ->
        val revenue = array[0] as? Double ?: 0.0
        val returns = array[7] as? Double ?: 0.0
        DashboardUiState(
            totalRevenue = revenue - returns,
            totalOrders = array[1] as Int,
            totalProducts = array[2] as Int,
            totalStock = array[3] as? Double ?: 0.0,
            topSellingProducts = array[4] as List<TopProduct>,
            revenueByCategory = array[5] as List<CategoryRevenue>,
            recentOrders = array[6] as List<OrderEntity>,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )
}
