package com.abdulmateen.pos_offline.feature.main.home.domain

import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    fun getAllProducts(): Flow<List<Product>>
    fun getProductById(productId: Long): Flow<Product?>
    suspend fun clearProducts()

    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)

    fun getAllCategories(): Flow<List<Category>>
    fun getCategoryById(categoryId: Long): Flow<Category?>
    suspend fun clearCategories()

    suspend fun insertUnit(unit: Unit)
    suspend fun updateUnit(unit: Unit)
    suspend fun deleteUnit(unit: Unit)

    fun getAllUnits(): Flow<List<Unit>>
    fun getUnitById(unitId: Long): Flow<Unit?>
    suspend fun clearUnits()


}