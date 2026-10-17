package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.data.database.dao.CreditDao
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.data.database.entities.CreditPaymentEntity
import com.abdulmateen.pos_offline.domain.repository.CreditRepository
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CreditRepositoryImpl(
    private val creditDao: CreditDao
) : CreditRepository {
    override suspend fun upsertCredit(credit: CreditEntity) = creditDao.upsertCredit(credit)
    override suspend fun deleteCredit(credit: CreditEntity) = creditDao.deleteCredit(credit)
    override fun getAllCredits(): Flow<List<CreditEntity>> = creditDao.getAllCredits()
    override fun getCreditsPaged(limit: Int, offset: Int): Flow<List<CreditEntity>> = creditDao.getCreditsPaged(limit, offset)
    override suspend fun getCreditById(creditId: Long): CreditEntity? = creditDao.getCreditById(creditId)
    override fun searchCredits(name: String): Flow<List<CreditEntity>> = creditDao.searchCreditsByCustomer(name)
    override suspend fun getCreditByOrderId(orderId: Long): CreditEntity? = creditDao.getCreditByOrderId(orderId)
    override suspend fun deleteCreditByOrderId(orderId: Long) = creditDao.deleteCreditByOrderId(orderId)
    override suspend fun getCustomerCreditBalance(customerId: Long?, customerName: String?): Double {
        val balance = if (customerId != null && customerId != 0L) {
            creditDao.getCreditBalanceByCustomerId(customerId)
        } else if (!customerName.isNullOrBlank()) {
            creditDao.getCreditBalanceByCustomerName(customerName)
        } else {
            null
        }
        return balance ?: 0.0
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun receiveCustomerPayment(customerId: Long?, customerName: String?, amount: Double): Double {
        val outstanding = if (customerId != null && customerId != 0L) {
            creditDao.getOutstandingCreditsByCustomerId(customerId)
        } else if (!customerName.isNullOrBlank()) {
            creditDao.getOutstandingCreditsByCustomerName(customerName)
        } else {
            emptyList()
        }

        val now = Clock.System.now().toEpochMilliseconds()
        var left = amount
        val updated = mutableListOf<CreditEntity>()
        for (credit in outstanding) {
            if (left <= 0.0) break
            val applied = minOf(left, credit.remainingAmount)
            left -= applied
            updated += credit.copy(
                paidAmount = credit.paidAmount + applied,
                remainingAmount = credit.remainingAmount - applied,
                lastUpdated = now
            )
        }
        if (updated.isNotEmpty()) {
            val first = updated.first()
            creditDao.recordPayment(
                payment = CreditPaymentEntity(
                    customerId = customerId?.takeIf { it != 0L } ?: updated.firstNotNullOfOrNull { it.customerId },
                    customerName = first.customerName,
                    creditId = updated.singleOrNull()?.creditId,
                    amount = amount - left,
                    date = now
                ),
                updatedCredits = updated
            )
        }
        return left
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun receiveCreditPayment(credit: CreditEntity, amount: Double) {
        val applied = minOf(amount, credit.remainingAmount)
        if (applied <= 0.0) return
        val now = Clock.System.now().toEpochMilliseconds()
        creditDao.recordPayment(
            payment = CreditPaymentEntity(
                customerId = credit.customerId,
                customerName = credit.customerName,
                creditId = credit.creditId,
                amount = applied,
                date = now
            ),
            updatedCredits = listOf(
                credit.copy(
                    paidAmount = credit.paidAmount + applied,
                    remainingAmount = credit.remainingAmount - applied,
                    lastUpdated = now
                )
            )
        )
    }

    override fun getCustomerPayments(customerId: Long?, customerName: String?): Flow<List<CreditPaymentEntity>> =
        if (customerId != null && customerId != 0L) {
            creditDao.getPaymentsByCustomerId(customerId)
        } else {
            creditDao.getPaymentsByCustomerName(customerName.orEmpty())
        }
}
