package com.abdulmateen.pos_offline.domain.use_cases.product

import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow

class GetProductList(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(): Flow<List<Product>> {
        return repository.getAllProducts()
    }
}

