package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.repository.CartRepository

class ClearCartItems(
    private val repository: CartRepository
) {
    suspend operator fun invoke() {
        repository.clearCartItems()
    }
}