package com.abdulmateen.pos_offline.data.mappers

import com.abdulmateen.pos_offline.data.database.entities.CustomerEntity
import com.abdulmateen.pos_offline.domain.models.Customer

fun CustomerEntity.toCustomer(): Customer {
    return Customer(
        customerId = customerId,
        name = name,
        phone = phone,
        email = email,
        address = address,
        createdAt = createdAt
    )
}

fun Customer.toCustomerEntity(): CustomerEntity {
    return CustomerEntity(
        customerId = customerId,
        name = name,
        phone = phone,
        email = email,
        address = address,
        createdAt = createdAt
    )
}
