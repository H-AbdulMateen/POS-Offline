package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.data.database.entities.ReturnEntity
import com.abdulmateen.pos_offline.data.database.entities.ReturnItemEntity
import kotlinx.coroutines.flow.Flow

interface ReturnRepository {
    suspend fun createReturn(returnEntity: ReturnEntity, items: List<ReturnItemEntity>)
    fun getAllReturns(): Flow<List<ReturnEntity>>
    fun getReturnsPaged(limit: Int, offset: Int): Flow<List<ReturnEntity>>
    fun getItemsForReturn(returnId: Long): Flow<List<ReturnItemEntity>>
    fun getTotalReturnsInRange(start: Long, end: Long): Flow<Double>
}
