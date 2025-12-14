package com.abdulmateen.pos_offline.feature.main.home.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.OrderEntity
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.OrderItemEntity

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    suspend fun createOrder(
        order: OrderEntity,
        items: List<OrderItemEntity>
    ) {
        val orderId = insertOrder(order)
        insertOrderItems(items.map { it.copy(orderId = orderId) })
    }
}
