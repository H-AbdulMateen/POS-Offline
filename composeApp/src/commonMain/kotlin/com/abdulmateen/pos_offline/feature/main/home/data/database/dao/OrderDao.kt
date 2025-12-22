package com.abdulmateen.pos_offline.feature.main.home.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.OrderEntity
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.OrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Upsert
    suspend fun insertOrUpdateOrder(order: OrderEntity): Long

    @Upsert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    suspend fun createOrder(
        order: OrderEntity,
        items: List<OrderItemEntity>
    ) {
        val orderId = insertOrUpdateOrder(order)
        insertOrderItems(items.map { it.copy(orderId = orderId) })
    }

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<OrderEntity>>


    @Transaction
    suspend fun updateOrder(
        order: OrderEntity,
        items: List<OrderItemEntity>
    ) {
        insertOrUpdateOrder(order)
        insertOrderItems(items)
    }

    @Transaction
    @Query("DELETE FROM orders WHERE orderId = :orderId")
    suspend fun deleteOrder(orderId: Long)
}
