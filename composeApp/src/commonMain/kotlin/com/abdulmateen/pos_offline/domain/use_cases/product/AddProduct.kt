package com.abdulmateen.pos_offline.domain.use_cases.product

import com.abdulmateen.pos_offline.domain.models.ProductDetail
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository

class AddProduct(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(product: ProductDetail) {
        repository.insertProduct(product)
    }
}