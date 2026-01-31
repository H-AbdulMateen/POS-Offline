package com.abdulmateen.pos_offline.domain.use_cases.product

import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.models.ProductUi
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.models.toProductUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductUiList(
    private val inventoryRepository: InventoryRepository
) {
    suspend operator fun invoke(): Flow<List<ProductUi>> {
        return inventoryRepository.getAllProducts().map { list ->
            list.map { it.toProductUi() }
        }
    }
}