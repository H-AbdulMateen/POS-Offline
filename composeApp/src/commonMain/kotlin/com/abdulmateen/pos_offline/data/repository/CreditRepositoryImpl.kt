package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.data.database.dao.CreditDao
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.domain.repository.CreditRepository
import kotlinx.coroutines.flow.Flow

class CreditRepositoryImpl(
    private val creditDao: CreditDao
) : CreditRepository {
    override suspend fun upsertCredit(credit: CreditEntity) = creditDao.upsertCredit(credit)
    override suspend fun deleteCredit(credit: CreditEntity) = creditDao.deleteCredit(credit)
    override fun getAllCredits(): Flow<List<CreditEntity>> = creditDao.getAllCredits()
    override suspend fun getCreditById(creditId: Long): CreditEntity? = creditDao.getCreditById(creditId)
    override fun searchCredits(name: String): Flow<List<CreditEntity>> = creditDao.searchCreditsByCustomer(name)
}
