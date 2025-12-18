package com.abdulmateen.pos_offline.feature.main.home.domain

import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import kotlinx.coroutines.flow.Flow
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
interface InventoryRepository {
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(productId: Long): Product?
    fun searchProductsByName(name: String): Flow<List<Product>>

    suspend fun clearProducts()

    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)

    fun getAllCategories(): Flow<List<Category>>
    fun getCategoryById(categoryId: Long): Flow<Category?>
    suspend fun clearCategories()

    suspend fun insertUnit(unit: ItemUnit)
    suspend fun updateUnit(unit: ItemUnit)
    suspend fun deleteUnit(unit: ItemUnit)

    fun getAllUnits(): Flow<List<ItemUnit>>
    fun getUnitById(unitId: Long): Flow<ItemUnit?>
    suspend fun clearUnits()


}