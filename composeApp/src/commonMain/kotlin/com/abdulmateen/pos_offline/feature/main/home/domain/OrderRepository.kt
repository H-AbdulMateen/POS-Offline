package com.abdulmateen.pos_offline.feature.main.home.domain

import com.abdulmateen.pos_offline.feature.main.home.domain.models.Order
import com.abdulmateen.pos_offline.feature.main.home.domain.models.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun createOrder(order: Order, orderItems: List<OrderItem>)
    suspend fun insertOrderItems(orderItems: List<OrderItem>)
    suspend fun updateOrder(order: Order, orderItems: List<OrderItem>)
    suspend fun deleteOrder(orderId: Long)
    suspend fun getOrderById(orderId: Long): Flow<Order?>
    suspend fun getAllOrders(): Flow<List<Order>>
}