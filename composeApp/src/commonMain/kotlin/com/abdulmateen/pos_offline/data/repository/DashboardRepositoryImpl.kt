package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.data.database.dao.CategoryRevenue
import com.abdulmateen.pos_offline.data.database.dao.DashboardDao
import com.abdulmateen.pos_offline.data.database.dao.ReturnDao
import com.abdulmateen.pos_offline.data.database.dao.TopProduct
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import com.abdulmateen.pos_offline.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow

class DashboardRepositoryImpl(
    private val dashboardDao: DashboardDao,
    private val returnDao: ReturnDao
) : DashboardRepository {
    override fun getTotalRevenue(): Flow<Double?> = dashboardDao.getTotalRevenue()
    override fun getTotalOrders(): Flow<Int> = dashboardDao.getTotalOrders()
    override fun getTotalProducts(): Flow<Int> = dashboardDao.getTotalProducts()
    override fun getTotalStock(): Flow<Double?> = dashboardDao.getTotalStock()
    override fun getTopSellingProducts(): Flow<List<TopProduct>> = dashboardDao.getTopSellingProducts()
    override fun getRevenueByCategory(): Flow<List<CategoryRevenue>> = dashboardDao.getRevenueByCategory()
    override fun getRecentOrders(): Flow<List<OrderEntity>> = dashboardDao.getRecentOrders()
    override fun getTotalReturns(): Flow<Double?> = returnDao.getTotalReturnsInRange(0, Long.MAX_VALUE)
}
