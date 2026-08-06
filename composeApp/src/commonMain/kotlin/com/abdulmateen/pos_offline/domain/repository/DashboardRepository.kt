package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.data.database.dao.CategoryRevenue
import com.abdulmateen.pos_offline.data.database.dao.TopProduct
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getTotalRevenue(): Flow<Double?>
    fun getTotalOrders(): Flow<Int>
    fun getTotalProducts(): Flow<Int>
    fun getTotalStock(): Flow<Double?>
    fun getTopSellingProducts(): Flow<List<TopProduct>>
    fun getRevenueByCategory(): Flow<List<CategoryRevenue>>
    fun getRecentOrders(): Flow<List<OrderEntity>>
}
