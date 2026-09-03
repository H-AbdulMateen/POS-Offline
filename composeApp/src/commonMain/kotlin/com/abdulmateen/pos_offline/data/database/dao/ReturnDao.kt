package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.abdulmateen.pos_offline.data.database.entities.ReturnEntity
import com.abdulmateen.pos_offline.data.database.entities.ReturnItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReturnDao {
    @Insert
    suspend fun insertReturn(returnEntity: ReturnEntity): Long

    @Insert
    suspend fun insertReturnItems(items: List<ReturnItemEntity>)

    @Query("SELECT * FROM returns ORDER BY createdAt DESC")
    fun getAllReturns(): Flow<List<ReturnEntity>>

    @Query("SELECT * FROM return_items WHERE returnId = :returnId")
    fun getItemsForReturn(returnId: Long): Flow<List<ReturnItemEntity>>

    @Query("SELECT SUM(totalReturnAmount) FROM returns WHERE createdAt >= :start AND createdAt <= :end")
    fun getTotalReturnsInRange(start: Long, end: Long): Flow<Double?>
}
