package com.abdulmateen.pos_offline.domain.use_cases.cart

import com.abdulmateen.pos_offline.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CalculateSubTotal(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): Flow<Double> {
        return repository.calculateSubTotal()
    }
}