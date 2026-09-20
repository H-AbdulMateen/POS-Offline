package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.abdulmateen.pos_offline.data.database.entities.CartEntity
import com.abdulmateen.pos_offline.data.database.entities.CartItemEntity
import com.abdulmateen.pos_offline.data.database.entities.CartWithItemsViewTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    // Cart
    @Insert
    suspend fun createCart(cart: CartEntity): Long

    @Query("SELECT * FROM cart LIMIT 1")
    suspend fun getActiveCart(): CartEntity?

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemsCount(): Flow<Int>

    @Query("DELETE FROM cart")
    suspend fun delete()

    // Cart Items
    @Insert
    suspend fun addCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE cartId = :cartId AND productId = :productId")
    suspend fun removeItem(cartId: Long, productId: Long)

    @Query("DELETE FROM cart_items WHERE cartId = :cartId")
    suspend fun clearItems(cartId: Long)

    // Relationships
    @Transaction
    @Query("SELECT * FROM cart WHERE cartId = :cartId")
    fun getCartWithItems(cartId: Long): Flow<CartWithItemsViewTable>

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    fun getCartItemByProductId(productId: Long): Flow<CartItemEntity?>

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE productId = :productId")
    suspend fun incrementInQuantity(productId: Long)

    @Query("UPDATE cart_items SET quantity = quantity - 1 WHERE productId = :productId")
    suspend fun decrementInQuantity(productId: Long)

    @Query("UPDATE cart_items SET price = :newPrice WHERE productId = :productId")
    suspend fun updatePrice(productId: Long, newPrice: Double)

    @Query("SELECT SUM(price * quantity) FROM cart_items")
    fun calculateSubTotal(): Flow<Double>

}
