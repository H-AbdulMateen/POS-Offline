package com.abdulmateen.pos_offline.data.repository

import androidx.room.Transaction
import com.abdulmateen.pos_offline.data.database.dao.ProductDao
import com.abdulmateen.pos_offline.data.database.dao.ReturnDao
import com.abdulmateen.pos_offline.data.database.entities.ReturnEntity
import com.abdulmateen.pos_offline.data.database.entities.ReturnItemEntity
import com.abdulmateen.pos_offline.domain.repository.ReturnRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReturnRepositoryImpl(
    private val returnDao: ReturnDao,
    private val productDao: ProductDao
) : ReturnRepository {
    
    override suspend fun createReturn(returnEntity: ReturnEntity, items: List<ReturnItemEntity>) {
        val returnId = returnDao.insertReturn(returnEntity)
        val itemsWithReturnId = items.map { it.copy(returnId = returnId) }
        returnDao.insertReturnItems(itemsWithReturnId)
        
        // Update stock
        items.forEach { item ->
            productDao.increaseStock(item.productId, item.quantity)
        }
    }

    override fun getAllReturns(): Flow<List<ReturnEntity>> = returnDao.getAllReturns()
    override fun getReturnsPaged(limit: Int, offset: Int): Flow<List<ReturnEntity>> = returnDao.getReturnsPaged(limit, offset)

    override fun getItemsForReturn(returnId: Long): Flow<List<ReturnItemEntity>> = 
        returnDao.getItemsForReturn(returnId)

    override fun getTotalReturnsInRange(start: Long, end: Long): Flow<Double> = 
        returnDao.getTotalReturnsInRange(start, end).map { it ?: 0.0 }
}
