package com.abdulmateen.pos_offline.feature.main.home.data.repository

import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.feature.main.home.domain.ProductRepository
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val productDao: ProductDao,
): ProductRepository {
    override suspend fun insertProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override fun getAllProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun getProductById(productId: Long): Flow<Product?> {
        TODO("Not yet implemented")
    }

    override suspend fun clearProducts() {
        TODO("Not yet implemented")
    }

}