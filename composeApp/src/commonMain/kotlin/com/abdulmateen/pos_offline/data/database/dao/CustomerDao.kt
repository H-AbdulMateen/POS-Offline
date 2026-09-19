package com.abdulmateen.pos_offline.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.data.database.entities.CustomerEntity
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Upsert
    suspend fun upsertCustomer(customer: CustomerEntity): Long

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers WHERE customerId = :customerId")
    suspend fun getCustomerById(customerId: Long): CustomerEntity?

    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchCustomers(query: String): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM orders WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getOrdersByCustomer(customerId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM credits WHERE customerId = :customerId ORDER BY date DESC")
    fun getCreditsByCustomer(customerId: Long): Flow<List<CreditEntity>>
}
