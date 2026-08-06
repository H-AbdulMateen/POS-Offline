package com.abdulmateen.pos_offline.feature.dashboard.presentation

import com.abdulmateen.pos_offline.data.database.dao.CategoryRevenue
import com.abdulmateen.pos_offline.data.database.dao.TopProduct
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity

data class DashboardUiState(
    val totalRevenue: Double = 0.0,
    val totalOrders: Int = 0,
    val totalProducts: Int = 0,
    val totalStock: Double = 0.0,
    val topSellingProducts: List<TopProduct> = emptyList(),
    val revenueByCategory: List<CategoryRevenue> = emptyList(),
    val recentOrders: List<OrderEntity> = emptyList(),
    val isLoading: Boolean = false
)
