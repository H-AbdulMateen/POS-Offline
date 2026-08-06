package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardDao {

    @Query("SELECT SUM(total) FROM orders")
    fun getTotalRevenue(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM orders")
    fun getTotalOrders(): Flow<Int>

    @Query("SELECT COUNT(*) FROM products")
    fun getTotalProducts(): Flow<Int>

    @Query("SELECT SUM(stock) FROM products")
    fun getTotalStock(): Flow<Double?>

    @Query("SELECT productName, SUM(quantity) as totalQty FROM order_items GROUP BY productId ORDER BY totalQty DESC LIMIT 5")
    fun getTopSellingProducts(): Flow<List<TopProduct>>

    @Query("""
        SELECT c.name as categoryName, SUM(oi.price * oi.quantity) as revenue 
        FROM order_items oi 
        JOIN products p ON oi.productId = p.productId 
        JOIN categories c ON p.categoryId = c.categoryId 
        GROUP BY c.categoryId 
        ORDER BY revenue DESC
    """)
    fun getRevenueByCategory(): Flow<List<CategoryRevenue>>

    @Query("SELECT * FROM orders ORDER BY createdAt DESC LIMIT 5")
    fun getRecentOrders(): Flow<List<com.abdulmateen.pos_offline.data.database.entities.OrderEntity>>
}

data class TopProduct(
    val productName: String,
    val totalQty: Double
)

data class CategoryRevenue(
    val categoryName: String,
    val revenue: Double
)
