package com.abdulmateen.pos_offline.domain.use_cases.product

import com.abdulmateen.pos_offline.domain.repository.InventoryRepository

class DeleteProduct(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(productId: Long): Boolean {
        return repository.deleteProduct(productId)
    }
}