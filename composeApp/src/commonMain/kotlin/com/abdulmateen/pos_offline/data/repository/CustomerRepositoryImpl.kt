package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.data.database.dao.CustomerDao
import com.abdulmateen.pos_offline.data.mappers.toCustomer
import com.abdulmateen.pos_offline.data.mappers.toCustomerEntity
import com.abdulmateen.pos_offline.data.mappers.toOrder
import com.abdulmateen.pos_offline.domain.models.Customer
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.repository.CustomerRepository
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomerRepositoryImpl(
    private val customerDao: CustomerDao
) : CustomerRepository {

    override suspend fun upsertCustomer(customer: Customer): Long {
        return customerDao.upsertCustomer(customer.toCustomerEntity())
    }

    override suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer.toCustomerEntity())
    }

    override suspend fun getCustomerById(customerId: Long): Customer? {
        return customerDao.getCustomerById(customerId)?.toCustomer()
    }

    override fun getAllCustomers(): Flow<List<Customer>> {
        return customerDao.getAllCustomers().map { entities ->
            entities.map { it.toCustomer() }
        }
    }

    override fun searchCustomers(query: String): Flow<List<Customer>> {
        return customerDao.searchCustomers(query).map { entities ->
            entities.map { it.toCustomer() }
        }
    }

    override fun getCustomerOrders(customerId: Long): Flow<List<Order>> {
        return customerDao.getOrdersByCustomer(customerId).map { entities ->
            entities.map { it.toOrder() }
        }
    }

    override fun getCustomerCredits(customerId: Long): Flow<List<CreditEntity>> {
        return customerDao.getCreditsByCustomer(customerId)
    }
}
