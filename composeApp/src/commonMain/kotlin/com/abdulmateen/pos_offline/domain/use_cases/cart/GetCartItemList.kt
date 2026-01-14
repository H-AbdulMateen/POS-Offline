package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class GetCartItemList(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): Flow<List<CartItem>> {
        return repository.getCartItems()
    }
}