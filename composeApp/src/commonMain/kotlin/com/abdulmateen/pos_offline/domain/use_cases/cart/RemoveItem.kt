package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.repository.CartRepository

class RemoveItem(
    private val repository: CartRepository
) {
    suspend operator fun invoke(productId: Long) {
        repository.removeCartItem(productId)
    }
}