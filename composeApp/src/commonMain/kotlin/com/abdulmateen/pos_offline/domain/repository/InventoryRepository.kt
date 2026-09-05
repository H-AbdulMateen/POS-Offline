package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.ProductDetail
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    suspend fun insertProduct(product: ProductDetail): Result<String, DataError.Local>
    suspend fun updateProduct(product: ProductDetail)
    suspend fun deleteProduct(productId: Long): Boolean
    fun getAllProducts(): Flow<List<Product>>
    fun getProductById(productId: Long): Flow<ProductDetail?>
    fun searchProduct(query: String): Flow<List<Product>>

    suspend fun clearProducts()

    suspend fun insertCategory(category: Category): Result<String, DataError.Local>
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)

    fun getAllCategories(): Flow<List<Category>>
    fun getCategoryById(categoryId: Long): Flow<Category?>
    suspend fun clearCategories()

    suspend fun insertUnit(unit: ItemUnit): Result<String, DataError.Local>
    suspend fun updateUnit(unit: ItemUnit)
    suspend fun deleteUnit(unit: ItemUnit)

    fun getAllUnits(): Flow<List<ItemUnit>>
    fun getUnitById(unitId: Long): Flow<ItemUnit?>
    suspend fun clearUnits()

    suspend fun reduceStock(productId: Long, qty: Double)

    suspend fun initializeDefaults()
}
