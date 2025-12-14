package com.abdulmateen.pos_offline.feature.main.home.data.repository

import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.feature.main.home.domain.OrderRepository
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Order
import com.abdulmateen.pos_offline.feature.main.home.domain.models.OrderItem
import kotlinx.coroutines.flow.Flow

class OrderRepositoryImpl(
    private val orderDao: OrderDao
): OrderRepository {
    override suspend fun createOrder(
        order: Order,
        orderItems: List<OrderItem>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun insertOrderItems(orderItems: List<OrderItem>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrder(
        order: Order,
        orderItems: List<OrderItem>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrder(orderId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderById(orderId: Long): Flow<Order?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllOrders(): Flow<List<Order>> {
        TODO("Not yet implemented")
    }
}