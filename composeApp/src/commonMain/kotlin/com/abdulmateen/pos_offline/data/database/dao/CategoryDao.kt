package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.abdulmateen.pos_offline.data.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Upsert
    suspend fun insertOrUpdate(category: CategoryEntity)
    @Upsert
    suspend fun upsertList(categories: List<CategoryEntity>)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE categoryId = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE name LIKE '%' || :name")
    fun getCategoryByName(name: String): Flow<CategoryEntity?>

    @Query("SELECT * FROM categories WHERE categoryId = :categoryId")
    fun getCategoryById(categoryId: Long): Flow<CategoryEntity?>

    @Query("DELETE FROM categories")
    suspend fun clearCategories()


}