package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.abdulmateen.pos_offline.data.database.entities.ProductEntity
import com.abdulmateen.pos_offline.data.database.entities.ProductWithCategoryAndUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Upsert
    suspend fun insertOrUpdate(product: ProductEntity)

    @Upsert
    suspend fun upsertList(products: List<ProductEntity>)


    @Query("SELECT * FROM products WHERE sku=:sku")
    suspend fun getProductBySku(sku: String): ProductEntity?

    @Query("SELECT * FROM products WHERE barcode=:barcode")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?


    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getProductsPaged(limit: Int, offset: Int): Flow<List<ProductEntity>>

    @Query("DELETE FROM products WHERE productId = :productId")
    suspend fun delete(productId: Long)

    @Query("DELETE FROM products WHERE productId = :productId")
    suspend fun deleteById(productId: Long)

    @Transaction
    @Query("SELECT * FROM products WHERE productId = :productId")
    fun getProductById(productId: Long): Flow<ProductWithCategoryAndUnit?>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%' OR barcode = :query ORDER BY name ASC")
    suspend fun searchProduct(query: String): ProductEntity

    @Query("SELECT * FROM products WHERE sku = :sku")
    suspend fun searchProductBySku(sku: String): ProductEntity

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    suspend fun searchProductByBarcode(barcode: String): ProductEntity


    @Query("SELECT products.* FROM products WHERE products.categoryId = :categoryId")
    fun getProductsByCategoryId(categoryId: Long): Flow<List<ProductEntity>>


    @Query("SELECT * FROM products WHERE name LIKE '%' || :name || '%'")
    fun filterProductsByQuery(name: String): Flow<List<ProductEntity>>

    @Query("DELETE FROM products")
    suspend fun clearProducts()

    @Query(
        """
            UPDATE products
            SET stock = stock - :qty
            WHERE productId = :productId
""")
    suspend fun reduceStock(productId: Long, qty: Double)

    @Query(
        """
            UPDATE products
            SET stock = stock + :qty
            WHERE productId = :productId
""")
    suspend fun increaseStock(productId: Long, qty: Double)

}
