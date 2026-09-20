package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.abdulmateen.pos_offline.data.database.entities.CartWithItemsViewTable
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderItemEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Upsert
    suspend fun insertOrUpdateOrder(order: OrderEntity): Long

    @Upsert
    suspend fun upsertOrderItem(item: OrderItemEntity)


    @Upsert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    suspend fun createOrder(
        order: OrderEntity,
        items: List<OrderItemEntity>
    ): Long {
        val orderId = insertOrUpdateOrder(order)
        insertOrderItems(items.map { it.copy(orderId = orderId) })
        return orderId
    }


    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    fun getOrdersPaged(limit: Int, offset: Int): Flow<List<OrderEntity>>


    @Transaction
    suspend fun updateOrder(
        order: OrderEntity,
        items: List<OrderItemEntity>
    ) {
        insertOrUpdateOrder(order)
        insertOrderItems(items)
    }

    @Transaction
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    fun getOrderWithItems(orderId: Long): Flow<OrderWithItems?>

    @Transaction
    @Query("DELETE FROM orders WHERE orderId = :orderId")
    suspend fun deleteOrder(orderId: Long)

    @Transaction
    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItems(orderId: Long)

    @Query("DELETE FROM order_items WHERE orderId = :orderId AND productId = :productId")
    suspend fun removeOrderItem(orderId: Long, productId: Long)

    @Query("SELECT SUM(total) FROM orders WHERE createdAt >= :start AND createdAt <= :end")
    fun getTotalRevenueInRange(start: Long, end: Long): Flow<Double?>

    @Query("SELECT * FROM orders WHERE createdAt >= :start AND createdAt <= :end ORDER BY createdAt DESC")
    fun getOrdersInRange(start: Long, end: Long): Flow<List<OrderEntity>>


    @Transaction
    suspend fun checkout(
        cart: CartWithItemsViewTable,
        paymentMethod: String
    ) {
        val orderId = insertOrUpdateOrder(
            OrderEntity(
                subTotal = cart.items.sumOf { it.cartItem.price * it.cartItem.quantity },
                tax = 0.0,
                discount = 0.0,
                total = cart.items.sumOf { it.cartItem.price * it.cartItem.quantity },
                paymentMethod = paymentMethod,
                paymentStatus = "PAID",
                customerName = ""
            )
        )

        val orderItems = cart.items.map {
            OrderItemEntity(
                orderId = orderId,
                productId = it.product.productId,
                productName = it.product.name,
                sku = it.product.sku,
                quantity = it.cartItem.quantity,
                price = it.cartItem.price,
                discount = it.cartItem.discount
            )
        }
        insertOrderItems(orderItems)
    }

}