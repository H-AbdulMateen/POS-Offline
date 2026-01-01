package com.abdulmateen.pos_offline.feature.main.home.data.repository

import com.abdulmateen.pos_offline.core.data.filestorage.ImageStorage
import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.CategoryDao
import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.UnitDao
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.ProductEntity
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toCategory
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toCategoryEntity
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toProduct
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toUnit
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toUnitEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.InventoryRepository
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ProductDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class InventoryRepositoryImpl(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val unitDao: UnitDao,
    private val imageStorage: ImageStorage
) : InventoryRepository {
    override suspend fun insertProduct(product: ProductDetail): Result<String, DataError.Local> {
        val productBySku = productDao.getProductBySku(product.sku)
        if (productBySku != null) {
            return Result.Error(DataError.Local.SKU_ALREADY_EXISTS)
        }
        val productByBarcode = productDao.getProductByBarcode(product.barcode)
        if (productByBarcode != null) {
            return Result.Error(DataError.Local.BARCODE_ALREADY_EXISTS)
        }



        val imagePath = product.photoBytes?.let { imageBytes ->
            imageStorage.saveImage(imageBytes)
        }

        productDao.insertOrUpdate(
            ProductEntity(
                name = product.name,
                description = product.description,
                sku = product.sku,
                barcode = product.barcode,
                purchasePrice = product.purchasePrice,
                salePrice = product.price,
                stock = product.stock,
                imagePath = imagePath,
                categoryId = product.category?.categoryId,
                unitId = product.unit?.unitId
            )
        )
        return Result.Success("Product inserted successfully")

    }

    override suspend fun updateProduct(product: ProductDetail) {
        val oldProduct = productDao.getProductById(product.productId)
        if (oldProduct != null) {
            val imagePath = product.photoBytes?.let { imageBytes ->
                imageStorage.saveImage(imageBytes)
            }

            productDao.insertOrUpdate(
                ProductEntity(
                    productId = product.productId,
                    name = product.name,
                    description = product.description,
                    sku = product.sku,
                    barcode = product.barcode,
                    purchasePrice = product.purchasePrice,
                    salePrice = product.price,
                    stock = product.stock,
                    imagePath = imagePath ?: oldProduct.product.imagePath,
                    categoryId = product.category?.categoryId,
                    unitId = product.unit?.unitId
                )
            )
        }
    }

    override suspend fun deleteProduct(productId: Long) {
        productDao.delete(productId = productId)
    }

    override fun getAllProducts(): Flow<List<Product>> =
        productDao.getAllProducts()
            .map { productEntities ->
                supervisorScope {
                    productEntities.map { productEntity ->
                        async { productEntity.toProduct(imageStorage = imageStorage) }
                    }.awaitAll()
                }
                productEntities.map { productEntity ->
                    productEntity.toProduct(imageStorage = imageStorage)
                }
            }

    override fun searchProductsByName(name: String): Flow<List<Product>> {
        return productDao.filterProductsByQuery(name = name).map { productEntities ->
            productEntities.map { productEntity ->
                productEntity.toProduct(imageStorage = imageStorage)
            }
        }
    }

    override suspend fun getProductById(productId: Long): ProductDetail? {
        return productDao.getProductById(productId)?.toProduct(imageStorage = imageStorage)
    }

    override suspend fun clearProducts() {
        productDao.clearProducts()
    }

    override suspend fun insertCategory(category: Category): Result<String, DataError.Local> {
        val categoryExists = categoryDao.getCategoryByName(category.name)
        if (categoryExists != null) {
            return Result.Error(DataError.Local.CATEGORY_ALREADY_EXISTS)
        }

        categoryDao.insertOrUpdate(category = category.toCategoryEntity())
        return Result.Success("Category added successfully")
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.insertOrUpdate(category = category.toCategoryEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category = category.toCategoryEntity())
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
            .map { categoryEntities -> categoryEntities.map { it.toCategory() } }
    }

    override fun getCategoryById(categoryId: Long): Flow<Category?> {
        return categoryDao.getCategoryById(categoryId)
            .map { it?.toCategory() }
    }

    override suspend fun clearCategories() {
        categoryDao.clearCategories()
    }

    override suspend fun insertUnit(unit: ItemUnit): Result<String, DataError.Local> {
        val unitExists = unitDao.getUnitByNameOrSymbol(unit.name, unit.symbol)
        if (unitExists != null) {
            return Result.Error(DataError.Local.UNIT_ALREADY_EXISTS)
        }
        unitDao.insertOrUpdate(unit.toUnitEntity())
        return Result.Success("Unit added successfully")
    }

    override suspend fun updateUnit(unit: ItemUnit) {
        unitDao.insertOrUpdate(unit.toUnitEntity())
    }

    override suspend fun deleteUnit(unit: ItemUnit) {
        unitDao.deleteUnit(unit.toUnitEntity())
    }

    override fun getAllUnits(): Flow<List<ItemUnit>> {
        return unitDao.getAllUnits()
            .map { unitEntities -> unitEntities.map { it.toUnit() } }
    }

    override fun getUnitById(unitId: Long): Flow<ItemUnit?> {
        return unitDao.getUnitById(unitId)
            .map { it?.toUnit() }
    }

    override suspend fun clearUnits() {
        unitDao.clearUnits()

    }

}