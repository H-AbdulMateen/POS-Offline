package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.data.database.entities.CartWithItemsViewTable
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.models.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun checkout(cart: CartWithItemsViewTable)

    suspend fun addOrderItem(orderItem: OrderItem)
    suspend fun removeOrderItem(orderId: Long, productId: Long)

    suspend fun insertOrderItems(orderItems: List<OrderItem>)
    suspend fun updateOrder(order: Order, orderItems: List<OrderItem>)
    suspend fun deleteOrder(orderId: Long)
    suspend fun getOrderById(orderId: Long): Order?
    suspend fun getAllOrders(): Flow<List<Order>>
}