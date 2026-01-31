package com.abdulmateen.pos_offline.domain.use_cases.product

import com.abdulmateen.pos_offline.domain.models.ProductDetail
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow

class GetProductDetail(
    private val inventoryRepository: InventoryRepository
) {
    operator fun invoke(productId: Long): Flow<ProductDetail?> {
        return inventoryRepository.getProductById(productId)
    }
}