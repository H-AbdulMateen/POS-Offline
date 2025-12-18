package com.abdulmateen.pos_offline.feature.main.home.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Upsert
    suspend fun insertOrUpdate(product: ProductEntity)

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("DELETE FROM products WHERE productId = :productId")
    suspend fun deleteById(productId: Long)

    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getProductById(productId: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE name = :name")
    suspend fun searchProductByName(name: String): ProductEntity

    @Query("SELECT * FROM products WHERE sku = :sku")
    suspend fun searchProductBySku(sku: String): ProductEntity

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun searchProductByBarcode(barcode: String): ProductEntity

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    suspend fun getProductsByCategoryId(categoryId: Long): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :name || '%'")
    fun filterProductsByQuery(name: String): Flow<List<ProductEntity>>

    @Query("DELETE FROM products")
    suspend fun clearProducts()
}
