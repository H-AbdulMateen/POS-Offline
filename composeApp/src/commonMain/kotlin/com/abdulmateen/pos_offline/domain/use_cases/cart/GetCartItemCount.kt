package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class GetCartItemCount(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getCartItemsCount()
    }
}