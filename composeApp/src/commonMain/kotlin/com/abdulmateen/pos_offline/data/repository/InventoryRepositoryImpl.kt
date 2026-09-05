package com.abdulmateen.pos_offline.data.repository

import co.touchlab.kermit.Logger
import com.abdulmateen.pos_offline.core.data.filestorage.ImageStorage
import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.data.database.dao.CategoryDao
import com.abdulmateen.pos_offline.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.data.database.dao.UnitDao
import com.abdulmateen.pos_offline.data.database.entities.CategoryEntity
import com.abdulmateen.pos_offline.data.database.entities.ProductEntity
import com.abdulmateen.pos_offline.data.database.entities.UnitEntity
import com.abdulmateen.pos_offline.data.mappers.toCategory
import com.abdulmateen.pos_offline.data.mappers.toCategoryEntity
import com.abdulmateen.pos_offline.data.mappers.toProduct
import com.abdulmateen.pos_offline.data.mappers.toUnit
import com.abdulmateen.pos_offline.data.mappers.toUnitEntity
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.ProductDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
        val oldProduct = productDao.getProductById(product.productId).firstOrNull()
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

    override suspend fun deleteProduct(productId: Long): Boolean {
        try {
            productDao.delete(productId = productId)
            return true
        }catch (ex: Exception){
            Logger.e("Exception: ${ex.message}")
            ex.printStackTrace()
            return false
        }
    }

    override fun getAllProducts(): Flow<List<Product>> =
        productDao.getAllProducts()
            .map { productEntities ->
                supervisorScope {
                    productEntities.map { productEntity ->
                        async { productEntity.toProduct(imageStorage = imageStorage) }
                    }.awaitAll()
                }
            }

    override fun searchProduct(query: String): Flow<List<Product>> {
        return productDao.filterProductsByQuery(name = query).map { productEntities ->
            productEntities.map { productEntity ->
                productEntity.toProduct(imageStorage = imageStorage)
            }
        }
    }

    override fun getProductById(productId: Long): Flow<ProductDetail?> {
        return productDao.getProductById(productId).map { it?.toProduct(imageStorage = imageStorage) }
    }

    override suspend fun clearProducts() {
        productDao.clearProducts()
    }

    override suspend fun insertCategory(category: Category): Result<String, DataError.Local> {
        val categoryExists = categoryDao.getCategoryByName(category.name).firstOrNull()
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
        val unitExists = unitDao.getUnitByNameOrSymbol(unit.name, unit.symbol).firstOrNull()
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
            .map { unitEntities ->
                unitEntities.map { it.toUnit() }
            }
    }

    override fun getUnitById(unitId: Long): Flow<ItemUnit?> {
        return unitDao.getUnitById(unitId)
            .map { it?.toUnit() }
    }

    override suspend fun clearUnits() {
        unitDao.clearUnits()
    }

    override suspend fun reduceStock(productId: Long, qty: Double) {
        productDao.reduceStock(productId = productId, qty = qty)
    }

    override suspend fun initializeDefaults() {
        val currentUnits = unitDao.getAllUnits().first()
        if (currentUnits.isEmpty()) {
            insertPrepopulatedUnits()
        }

        val currentCategories = categoryDao.getAllCategories().first()
        if (currentCategories.isEmpty()) {
            insertPrepopulatedCategories()
        }
        
        val currentProducts = productDao.getAllProducts().first()
        if (currentProducts.isEmpty()) {
            insertPrepopulatedProducts()
        }
    }

    private suspend fun insertPrepopulatedProducts() {
        productDao.upsertList(
            listOf(
                ProductEntity(
                    name = "Apple",
                    description = "Fresh and juicy apples",
                    sku = "SKU001",
                    barcode = "123456789012",
                    purchasePrice = 50.0,
                    salePrice = 100.0,
                    stock = 100.0,
                    discount = 0.0,
                    categoryId = 2,
                    unitId = 1
                ),
                ProductEntity(
                    name = "Banana",
                    description = "Ripe and sweet bananas",
                    sku = "SKU002",
                    barcode = "234567890123",
                    purchasePrice = 300.0,
                    salePrice = 350.0,
                    stock = 50.0,
                    discount = 0.0,
                    categoryId = 2,
                    unitId = 6
                ),
                ProductEntity(
                    name = "Milk",
                    description = "Fresh milk from the cow",
                    sku = "SKU003",
                    barcode = "345678901234",
                    purchasePrice = 1000.0,
                    salePrice = 1100.0,
                    stock = 20.0,
                    discount = 0.0,
                    categoryId = 5,
                    unitId = 3
                ),
                ProductEntity(
                    name = "Orange",
                    description = "Fresh and juicy oranges",
                    sku = "SKU004",
                    barcode = "456789012345",
                    purchasePrice = 50.0,
                    salePrice = 100.0,
                    stock = 100.0,
                    discount = 0.0,
                    categoryId = 2,
                    unitId = 1
                ),
                ProductEntity(
                    name = "Tomato",
                    description = "Fresh and juicy tomatoes",
                    sku = "SKU005",
                    barcode = "567890123456",
                    purchasePrice = 50.0,
                    salePrice = 100.0,
                    stock = 100.0,
                    discount = 0.0,
                    categoryId = 3,
                    unitId = 1
                ),
                ProductEntity(
                    name = "Potato",
                    description = "Fresh and juicy potatoes",
                    sku = "SKU006",
                    barcode = "678901234567",
                    purchasePrice = 50.0,
                    salePrice = 100.0,
                    stock = 100.0,
                    discount = 0.0,
                    categoryId = 3,
                    unitId = 1
                ),
                ProductEntity(
                    name = "Carrot",
                    description = "Fresh and juicy carrots",
                    sku = "SKU007",
                    barcode = "789012345678",
                    purchasePrice = 50.0,
                    salePrice = 100.0,
                    stock = 100.0,
                    discount = 0.0,
                    categoryId = 3,
                    unitId = 1
                ),
                ProductEntity(
                    name = "Onion",
                    description = "Fresh and juicy onions",
                    sku = "SKU008",
                    barcode = "890123456789",
                    purchasePrice = 50.0,
                    salePrice = 100.0,
                    stock = 100.0,
                    discount = 0.0,
                    categoryId = 3,
                    unitId = 1
                ),
                ProductEntity(
                    name = "Garlic",
                    description = "Fresh and juicy garlic",
                    sku = "SKU009",
                    barcode = "901234567890",
                    purchasePrice = 50.0,
                    salePrice = 100.0,
                    stock = 100.0,
                    discount = 0.0,
                    categoryId = 3,
                    unitId = 1
                ),
                ProductEntity(
                    name = "Ginger",
                    description = "Fresh and juicy ginger",
                    sku = "SKU010",
                    barcode = "012345678901",
                    purchasePrice = 20050.0,
                    salePrice = 21000.0,
                    stock = 500.0,
                    discount = 0.0,
                    categoryId = 3,
                    unitId = 1
                )
            )
        )
    }

    private suspend fun insertPrepopulatedCategories() {
        categoryDao.upsertList(
            listOf(
                CategoryEntity(categoryId = 1, name = "Clothing"),
                CategoryEntity(categoryId = 2, name = "Fruit"),
                CategoryEntity(categoryId = 3, name = "Vegetables"),
                CategoryEntity(categoryId = 4, name = "Bakery"),
                CategoryEntity(categoryId = 5, name = "Dairy"),
                CategoryEntity(categoryId = 6, name = "Meat"),
                CategoryEntity(categoryId = 7, name = "Beverages"),
                CategoryEntity(categoryId = 8, name = "Snacks")
            )
        )
    }

    private suspend fun insertPrepopulatedUnits() {
        unitDao.upsertList(
            listOf(
                UnitEntity(unitId = 1, name = "Kilogram", symbol = "kg"),
                UnitEntity(unitId = 2, name = "Gram", symbol = "g"),
                UnitEntity(unitId = 3, name = "Liter", symbol = "l"),
                UnitEntity(unitId = 4,name = "Milliliter", symbol = "ml"),
                UnitEntity(unitId = 5, name = "Piece", symbol = "pc"),
                UnitEntity(unitId = 6, name = "Dozen", symbol = "dz"),
                UnitEntity(unitId = 7, name = "Pound", symbol = "lb"),
                UnitEntity(unitId = 8, name = "Ounce", symbol = "oz"),
                UnitEntity(unitId = 9, name = "Box", symbol = "bx"),
                UnitEntity(unitId = 10, name = "Packet", symbol = "pkt"),
                UnitEntity(unitId = 11, name = "Can", symbol = "can"),
                UnitEntity(unitId = 12, name = "Bag", symbol = "bag"),
                UnitEntity(unitId = 13, name = "Roll", symbol = "roll"),
                UnitEntity(unitId = 14, name = "Jar", symbol = "jar"),
                UnitEntity(unitId = 15, name = "Carton", symbol = "ct"),
                UnitEntity(unitId = 16, name = "Unit", symbol = "unit")
            )
        )
    }
}
