package com.abdulmateen.pos_offline.domain.repository

import com.abdulmateen.pos_offline.domain.models.Customer
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun upsertCustomer(customer: Customer): Long
    suspend fun deleteCustomer(customer: Customer)
    suspend fun getCustomerById(customerId: Long): Customer?
    fun getAllCustomers(): Flow<List<Customer>>
    fun searchCustomers(query: String): Flow<List<Customer>>
    
    // Ledger related
    fun getCustomerOrders(customerId: Long): Flow<List<Order>>
    fun getCustomerCredits(customerId: Long): Flow<List<CreditEntity>>
}
