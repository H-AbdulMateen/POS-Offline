package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.data.database.entities.CartWithItemsViewTable
import com.abdulmateen.pos_offline.data.mappers.toOrder
import com.abdulmateen.pos_offline.data.mappers.toOrderEntity
import com.abdulmateen.pos_offline.data.mappers.toOrderItemEntity
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.models.OrderItem
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val orderDao: OrderDao
): OrderRepository {

    override suspend fun checkout(
        cart: CartWithItemsViewTable
    ) {
        orderDao.checkout(
            cart = cart,
            paymentMethod = "CASH"
        )
    }

    override suspend fun addOrderItem(orderItem: OrderItem) {
        orderDao.upsertOrderItem(orderItem.toOrderItemEntity())
    }

    override suspend fun removeOrderItem(orderId: Long, productId: Long) {
        orderDao.removeOrderItem(orderId, productId)
    }

    override suspend fun insertOrderItems(orderItems: List<OrderItem>) {
        orderDao.insertOrderItems(orderItems.map { it.toOrderItemEntity() })
    }

    override suspend fun updateOrder(
        order: Order,
        orderItems: List<OrderItem>
    ) {
        orderDao.insertOrUpdateOrder(order.toOrderEntity())
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