package com.abdulmateen.pos_offline.feature.main.home.domain

import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import kotlinx.coroutines.flow.Flow
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ProductDetail

interface InventoryRepository {
    suspend fun insertProduct(product: ProductDetail): Result<String, DataError.Local>
    suspend fun updateProduct(product: ProductDetail)
    suspend fun deleteProduct(productId: Long)
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(productId: Long): ProductDetail?
    fun searchProductsByName(name: String): Flow<List<Product>>

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


}