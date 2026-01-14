package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.repository.CartRepository

class IncrementInQuantity(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: Long) {
        cartRepository.incrementInQuantity(productId = productId)
    }
}