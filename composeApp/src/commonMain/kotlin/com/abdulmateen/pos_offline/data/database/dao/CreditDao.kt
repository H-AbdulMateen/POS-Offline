package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.*
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditDao {
    @Upsert
    suspend fun upsertCredit(credit: CreditEntity): Long

    @Delete
    suspend fun deleteCredit(credit: CreditEntity)

    @Query("SELECT * FROM credits ORDER BY lastUpdated DESC")
    fun getAllCredits(): Flow<List<CreditEntity>>

    @Query("SELECT * FROM credits WHERE creditId = :creditId")
    suspend fun getCreditById(creditId: Long): CreditEntity?

    @Query("SELECT * FROM credits WHERE customerName LIKE '%' || :name || '%'")
    fun searchCreditsByCustomer(name: String): Flow<List<CreditEntity>>
}
