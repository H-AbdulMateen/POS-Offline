package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.repository.CartRepository

class UpdateCartItemPrice(
    private val repository: CartRepository
) {
    suspend operator fun invoke(productId: Long, newPrice: Double) {
        repository.updatePrice(productId, newPrice)
    }
}
