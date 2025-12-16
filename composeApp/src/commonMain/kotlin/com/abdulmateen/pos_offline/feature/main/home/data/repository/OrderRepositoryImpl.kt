package com.abdulmateen.pos_offline.feature.main.home.data.repository

import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toOrder
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toOrderEntity
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toOrderItemEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.OrderRepository
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Order
import com.abdulmateen.pos_offline.feature.main.home.domain.models.OrderItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val orderDao: OrderDao
): OrderRepository {
    override suspend fun createOrder(
        order: Order,
        orderItems: List<OrderItem>
    ) {
        orderDao.insertOrder(order.toOrderEntity())
        orderDao.insertOrderItems(orderItems.map { it.toOrderItemEntity() })
    }

    override suspend fun insertOrderItems(orderItems: List<OrderItem>) {
        orderDao.insertOrderItems(orderItems.map { it.toOrderItemEntity() })
    }

    override suspend fun updateOrder(
        order: Order,
        orderItems: List<OrderItem>
    ) {
        orderDao.insertOrder(order.toOrderEntity())
        orderDao.insertOrderItems(orderItems.map { it.toOrderItemEntity() })
    }

    override suspend fun deleteOrder(orderId: Long) {
        orderDao.deleteOrder(orderId)
    }

    override suspend fun getOrderById(orderId: Long): Order? {
//        return orderDao.getOrderById(orderId).toOrder()
        return orderDao.getOrderById(orderId)?.toOrder()
    }

    override suspend fun getAllOrders(): Flow<List<Order>> =
        orderDao.getAllOrders().map { orderEntities -> orderEntities.map { it.toOrder() } }

}