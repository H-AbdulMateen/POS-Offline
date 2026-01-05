package com.abdulmateen.pos_offline.domain.use_cases.product

import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow

class SearchProductByName(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(query: String): Flow<List<Product>> {
        return repository.searchProduct(query)
    }
}