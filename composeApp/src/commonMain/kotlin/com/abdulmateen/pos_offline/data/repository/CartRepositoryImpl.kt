package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.data.database.dao.CartDao
import com.abdulmateen.pos_offline.data.database.entities.CartEntity
import com.abdulmateen.pos_offline.data.mappers.toCartItem
import com.abdulmateen.pos_offline.data.mappers.toCartItemEntity
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.domain.repository.CartRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {
    override suspend fun addToCart(cartItem: CartItem) {
        val cart = cartDao.getActiveCart() ?: CartEntity().also {
            val cartId = cartDao.createCart(it)
            it.copy(cartId = cartId)
        }
        val productExists = cartDao.getCartItemByProductId(cartItem.productId).firstOrNull()
        if (productExists != null) {
            incrementInQuantity(productId = productExists.productId)
        }else{
            cartDao.addCartItem(cartItem.toCartItemEntity().copy(cartId = cart.cartId))
        }

    }

    override fun getCartItemsCount(): Flow<Int> {
        return cartDao.getCartItemsCount()
    }

    override suspend fun removeCartItem(productId: Long) {
        val cartId = cartDao.getActiveCart()?.cartId ?: return
        cartDao.removeItem(cartId = cartId, productId = productId)
    }

    override suspend fun clearCartItems() {
        val cartId = cartDao.getActiveCart()?.cartId ?: return
        cartDao.clearItems(cartId = cartId)
    }

    override suspend fun deleteCart() {
        cartDao.delete()
    }

    override suspend fun getCartItems(): Flow<List<CartItem>> {
        val cartId = cartDao.getActiveCart()?.cartId ?: throw Exception("No active cart found")
        return cartDao.getCartWithItems(cartId = cartId)
            .map { (cart, items) ->
                supervisorScope {
                    items.map { item ->
                        async { item.cartItem.toCartItem() }
                    }.awaitAll()
                }
            }
    }

    override suspend fun incrementInQuantity(productId: Long) {
        cartDao.incrementInQuantity(productId = productId)
    }

    override suspend fun decrementInQuantity(productId: Long) {
        val product = cartDao.getCartItemByProductId(productId).firstOrNull()
        if (product != null && product.quantity > 1.0) {
            cartDao.decrementInQuantity(productId = productId)
        }
    }

}