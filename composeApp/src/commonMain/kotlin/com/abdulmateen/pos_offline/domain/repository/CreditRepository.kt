package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import kotlinx.coroutines.flow.Flow

interface CreditRepository {
    suspend fun upsertCredit(credit: CreditEntity): Long
    suspend fun deleteCredit(credit: CreditEntity)
    fun getAllCredits(): Flow<List<CreditEntity>>
    fun getCreditsPaged(limit: Int, offset: Int): Flow<List<CreditEntity>>
    suspend fun getCreditById(creditId: Long): CreditEntity?
    fun searchCredits(name: String): Flow<List<CreditEntity>>
    suspend fun getCreditByOrderId(orderId: Long): CreditEntity?
}
