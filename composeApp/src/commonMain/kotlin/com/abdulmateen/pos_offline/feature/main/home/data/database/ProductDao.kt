package com.abdulmateen.pos_offline.feature.main.home.data.database
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Upsert
    suspend fun upsert(product: ProductEntity)

    @Query("SELECT * FROM ProductEntity")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE id = :id")
    suspend fun getProduct(id: Int): ProductEntity?

    @Query("SELECT * FROM ProductEntity WHERE title LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT EXISTS(SELECT * FROM ProductEntity WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>

    @Query("SELECT * FROM ProductEntity WHERE isFavourite = 1")
    fun getFavouriteProducts(): Flow<List<ProductEntity>>

    @Query("UPDATE ProductEntity SET isFavourite = 1 WHERE id = :id")
    suspend fun markAsFavourite(id: Int)

    @Query("UPDATE ProductEntity SET isFavourite = 0 WHERE id = :id")
    suspend fun removeFromFavourite(id: Int)

    @Query("DELETE FROM ProductEntity WHERE id = :id")
    suspend fun deleteProduct(id: Int)


    @Query("DELETE FROM ProductEntity")
    suspend fun deleteAllProducts()


}