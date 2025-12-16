package com.abdulmateen.pos_offline.feature.main.home.data.repository

import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.CategoryDao
import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.UnitDao
import com.abdulmateen.pos_offline.feature.main.home.domain.InventoryRepository
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import kotlinx.coroutines.flow.Flow

class InventoryRepositoryImpl(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val unitDao: UnitDao
): InventoryRepository {
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

    override suspend fun insertCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override fun getAllCategories(): Flow<List<Category>> {
        TODO("Not yet implemented")
    }

    override fun getCategoryById(categoryId: Long): Flow<Category?> {
        TODO("Not yet implemented")
    }

    override suspend fun clearCategories() {
        TODO("Not yet implemented")
    }

    override suspend fun insertUnit(unit: Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUnit(unit: Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUnit(unit: Unit) {
        TODO("Not yet implemented")
    }

    override fun getAllUnits(): Flow<List<Unit>> {
        TODO("Not yet implemented")
    }

    override fun getUnitById(unitId: Long): Flow<Unit?> {
        TODO("Not yet implemented")
    }

    override suspend fun clearUnits() {
        TODO("Not yet implemented")
    }

}