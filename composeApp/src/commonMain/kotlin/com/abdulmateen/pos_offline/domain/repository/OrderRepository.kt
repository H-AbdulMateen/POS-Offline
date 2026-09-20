package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.models.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun checkout(
        cartItems: List<com.abdulmateen.pos_offline.domain.models.CartItem>,
        subTotal: Double,
        discount: Double,
        tax: Double,
        total: Double,
        customerId: Long?,
        customerName: String?,
        customerPhone: String?,
        paymentMethod: String
    ): Long

    suspend fun addOrderItem(orderItem: OrderItem)
    suspend fun removeOrderItem(orderId: Long, productId: Long)

    suspend fun insertOrderItems(orderItems: List<OrderItem>)
    suspend fun updateOrder(order: Order, orderItems: List<OrderItem>)
    suspend fun deleteOrder(orderId: Long)
    suspend fun getOrderById(orderId: Long): Order?
    fun getAllOrders(): Flow<List<Order>>
    fun getOrdersPaged(limit: Int, offset: Int): Flow<List<Order>>
    fun getOrderWithItems(orderId: Long): Flow<com.abdulmateen.pos_offline.data.database.entities.OrderWithItems?>
}
