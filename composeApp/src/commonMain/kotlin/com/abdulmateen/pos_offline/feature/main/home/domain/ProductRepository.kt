package com.abdulmateen.pos_offline.feature.main.home.domain

import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    fun getAllProducts(): Flow<List<Product>>
    fun getProductById(productId: Long): Flow<Product?>
    suspend fun clearProducts()
}