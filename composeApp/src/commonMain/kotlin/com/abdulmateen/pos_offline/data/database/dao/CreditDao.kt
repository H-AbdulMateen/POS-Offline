package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.*
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.data.database.entities.CreditPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditDao {
    @Upsert
    suspend fun upsertCredit(credit: CreditEntity): Long

    @Delete
    suspend fun deleteCredit(credit: CreditEntity)

    @Query("SELECT * FROM credits ORDER BY lastUpdated DESC")
    fun getAllCredits(): Flow<List<CreditEntity>>

    @Query("SELECT * FROM credits ORDER BY lastUpdated DESC LIMIT :limit OFFSET :offset")
    fun getCreditsPaged(limit: Int, offset: Int): Flow<List<CreditEntity>>

    @Query("SELECT * FROM credits WHERE creditId = :creditId")
    suspend fun getCreditById(creditId: Long): CreditEntity?

    @Query("SELECT * FROM credits WHERE customerName LIKE '%' || :name || '%'")
    fun searchCreditsByCustomer(name: String): Flow<List<CreditEntity>>

    @Query("SELECT * FROM credits WHERE orderId = :orderId")
    suspend fun getCreditByOrderId(orderId: Long): CreditEntity?

    @Query("DELETE FROM credits WHERE orderId = :orderId")
    suspend fun deleteCreditByOrderId(orderId: Long)

    @Query("SELECT SUM(remainingAmount) FROM credits WHERE customerId = :customerId")
    suspend fun getCreditBalanceByCustomerId(customerId: Long): Double?

    @Query("SELECT SUM(remainingAmount) FROM credits WHERE LOWER(customerName) = LOWER(:customerName)")
    suspend fun getCreditBalanceByCustomerName(customerName: String): Double?

    @Upsert
    suspend fun upsertCredits(credits: List<CreditEntity>)

    @Query("SELECT * FROM credits WHERE customerId = :customerId AND remainingAmount > 0 ORDER BY date ASC")
    suspend fun getOutstandingCreditsByCustomerId(customerId: Long): List<CreditEntity>

    @Query("SELECT * FROM credits WHERE LOWER(customerName) = LOWER(:customerName) AND remainingAmount > 0 ORDER BY date ASC")
    suspend fun getOutstandingCreditsByCustomerName(customerName: String): List<CreditEntity>

    @Query("DELETE FROM credits")
    suspend fun clearCredits()

    @Insert
    suspend fun insertPayment(payment: CreditPaymentEntity): Long

    @Transaction
    suspend fun recordPayment(payment: CreditPaymentEntity, updatedCredits: List<CreditEntity>) {
        upsertCredits(updatedCredits)
        insertPayment(payment)
    }

    @Query("SELECT * FROM credit_payments WHERE customerId = :customerId ORDER BY date DESC")
    fun getPaymentsByCustomerId(customerId: Long): Flow<List<CreditPaymentEntity>>

    @Query("SELECT * FROM credit_payments WHERE LOWER(customerName) = LOWER(:customerName) ORDER BY date DESC")
    fun getPaymentsByCustomerName(customerName: String): Flow<List<CreditPaymentEntity>>

    @Query("DELETE FROM credit_payments")
    suspend fun clearPayments()
}
