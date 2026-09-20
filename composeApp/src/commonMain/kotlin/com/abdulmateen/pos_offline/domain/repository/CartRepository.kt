package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.data.database.entities.CartWithItemsViewTable
import com.abdulmateen.pos_offline.domain.models.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun addToCart(cartItem: CartItem)
    fun getCartItemsCount(): Flow<Int>
    suspend fun removeCartItem(productId: Long)

    suspend fun clearCartItems()
    suspend fun deleteCart()

    suspend fun getCartItems(): Flow<List<CartItem>>

    suspend fun incrementInQuantity(productId: Long)
    suspend fun decrementInQuantity(productId: Long)
    suspend fun updatePrice(productId: Long, newPrice: Double)
    fun calculateSubTotal(): Flow<Double>

}