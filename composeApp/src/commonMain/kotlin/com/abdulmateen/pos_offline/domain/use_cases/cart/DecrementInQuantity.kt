package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.repository.CartRepository

class DecrementInQuantity(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: Long) {
        cartRepository.decrementInQuantity(productId = productId)
    }
}