package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.models.OrderItem
import com.abdulmateen.pos_offline.domain.repository.OrderRepository

class AddItemToCart(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(item: OrderItem) {
    }
}