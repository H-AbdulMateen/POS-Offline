package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderItemEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderWithItems
import com.abdulmateen.pos_offline.data.mappers.toOrder
import com.abdulmateen.pos_offline.data.mappers.toOrderEntity
import com.abdulmateen.pos_offline.data.mappers.toOrderItemEntity
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.models.OrderItem
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val orderDao: OrderDao,
    private val productDao: ProductDao
): OrderRepository {

    override suspend fun checkout(
        cartItems: List<CartItem>,
        subTotal: Double,
        discount: Double,
        tax: Double,
        total: Double,
        customerId: Long?,
        customerName: String?,
        customerPhone: String?,
        paymentMethod: String
    ): Long {
        val orderEntity = OrderEntity(
            customerId = customerId,
            customerName = customerName,
            customerPhone = customerPhone,
            subTotal = subTotal,
            discount = discount,
            tax = tax,
            total = total,
            paymentMethod = paymentMethod,
            paymentStatus = "PAID"
        )
        val orderItems = cartItems.map {
            OrderItemEntity(
                orderId = 0,
                productId = it.productId,
                productName = it.productName,
                sku = it.sku,
                quantity = it.quantity,
                price = it.unitPrice,
                discount = it.discount
            )
        }
        val orderId = orderDao.createOrder(orderEntity, orderItems)
        cartItems.forEach { item ->
            productDao.reduceStock(item.productId, item.quantity)
        }
        return orderId
    }

    override suspend fun updateCheckout(
        orderId: Long,
        cartItems: List<CartItem>,
        subTotal: Double,
        discount: Double,
        tax: Double,
        total: Double,
        customerId: Long?,
        customerName: String?,
        customerPhone: String?,
        paymentMethod: String
    ) {
        val previousOrderWithItems = orderDao.getOrderWithItemsDirect(orderId)
        previousOrderWithItems?.items?.forEach { item ->
            productDao.increaseStock(item.productId, item.quantity)
        }

        val orderEntity = OrderEntity(
            orderId = orderId,
            customerId = customerId,
            customerName = customerName,
            customerPhone = customerPhone,
            subTotal = subTotal,
            discount = discount,
            tax = tax,
            total = total,
            paymentMethod = paymentMethod,
            paymentStatus = "PAID"
        )
        val orderItems = cartItems.map {
            OrderItemEntity(
                orderId = orderId,
                productId = it.productId,
                productName = it.productName,
                sku = it.sku,
                quantity = it.quantity,
                price = it.unitPrice,
                discount = it.discount
            )
        }
        orderDao.updateOrder(orderEntity, orderItems)

        cartItems.forEach { item ->
            productDao.reduceStock(item.productId, item.quantity)
        }
    }

    override suspend fun addOrderItem(orderItem: OrderItem) {
        orderDao.upsertOrderItem(orderItem.toOrderItemEntity())
        productDao.reduceStock(orderItem.productId, orderItem.quantity)
    }

    override suspend fun removeOrderItem(orderId: Long, productId: Long) {
        val previousOrderWithItems = orderDao.getOrderWithItemsDirect(orderId)
        val targetItem = previousOrderWithItems?.items?.find { it.productId == productId }
        if (targetItem != null) {
            productDao.increaseStock(targetItem.productId, targetItem.quantity)
        }
        orderDao.removeOrderItem(orderId, productId)
    }

    override suspend fun insertOrderItems(orderItems: List<OrderItem>) {
        orderDao.insertOrderItems(orderItems.map { it.toOrderItemEntity() })
        orderItems.forEach { item ->
            productDao.reduceStock(item.productId, item.quantity)
        }
    }

    override suspend fun updateOrder(
        order: Order,
        orderItems: List<OrderItem>
    ) {
        val previousOrderWithItems = orderDao.getOrderWithItemsDirect(order.orderId)
        previousOrderWithItems?.items?.forEach { item ->
            productDao.increaseStock(item.productId, item.quantity)
        }

        orderDao.insertOrUpdateOrder(order.toOrderEntity())
        orderDao.insertOrderItems(orderItems.map { it.toOrderItemEntity() })

        orderItems.forEach { item ->
            productDao.reduceStock(item.productId, item.quantity)
        }
    }

    override suspend fun deleteOrder(orderId: Long) {
        val previousOrderWithItems = orderDao.getOrderWithItemsDirect(orderId)
        previousOrderWithItems?.items?.forEach { item ->
            productDao.increaseStock(item.productId, item.quantity)
        }
        orderDao.deleteOrder(orderId)
    }

    override suspend fun getOrderById(orderId: Long): Order? {
        return orderDao.getOrderById(orderId)?.toOrder()
    }

    override fun getAllOrders(): Flow<List<Order>> =
        orderDao.getAllOrders().map { orderEntities -> orderEntities.map { it.toOrder() } }

    override fun getOrdersPaged(limit: Int, offset: Int): Flow<List<Order>> =
        orderDao.getOrdersPaged(limit, offset).map { orderEntities -> orderEntities.map { it.toOrder() } }

    override fun getOrderWithItems(orderId: Long): Flow<OrderWithItems?> =
        orderDao.getOrderWithItems(orderId)

    override suspend fun getOrderWithItemsDirect(orderId: Long): OrderWithItems? =
        orderDao.getOrderWithItemsDirect(orderId)

}
