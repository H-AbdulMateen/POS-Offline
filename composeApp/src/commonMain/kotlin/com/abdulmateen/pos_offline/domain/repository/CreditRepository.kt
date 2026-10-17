package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.data.database.entities.CreditPaymentEntity
import kotlinx.coroutines.flow.Flow

interface CreditRepository {
    suspend fun upsertCredit(credit: CreditEntity): Long
    suspend fun deleteCredit(credit: CreditEntity)
    fun getAllCredits(): Flow<List<CreditEntity>>
    fun getCreditsPaged(limit: Int, offset: Int): Flow<List<CreditEntity>>
    suspend fun getCreditById(creditId: Long): CreditEntity?
    fun searchCredits(name: String): Flow<List<CreditEntity>>
    suspend fun getCreditByOrderId(orderId: Long): CreditEntity?
    suspend fun deleteCreditByOrderId(orderId: Long)
    suspend fun getCustomerCreditBalance(customerId: Long?, customerName: String?): Double
    /** Applies [amount] to the customer's oldest outstanding credits first. Returns the unapplied remainder. */
    suspend fun receiveCustomerPayment(customerId: Long?, customerName: String?, amount: Double): Double
    /** Applies [amount] to one specific credit and records it in payment history. */
    suspend fun receiveCreditPayment(credit: CreditEntity, amount: Double)
    fun getCustomerPayments(customerId: Long?, customerName: String?): Flow<List<CreditPaymentEntity>>
}
