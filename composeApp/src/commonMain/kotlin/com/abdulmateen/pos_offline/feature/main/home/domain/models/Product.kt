package com.abdulmateen.pos_offline.feature.main.home.domain.models

data class Product(
    val productId: Long = 0,
    val name: String,
    val description: String? = null,
    val sku: String,
    val barcode: String,
    val purchasePrice: Double,
    val salePrice: Double,
    val quantity: Double,
    val imageUrl: String? = null,
    val categoryId: Long?,
    val unitId: Long,
)

val dummyProducts = listOf(
    Product(productId = 1, name = "Apple", sku = "SKU001", barcode = "1234567890", purchasePrice = 1.99, salePrice = 2.49, quantity = 10.0, categoryId = 1, unitId = 1),
    Product(productId = 2, name = "Banana", sku = "SKU002", barcode = "09123", purchasePrice = 0.99, salePrice = 1.49, quantity = 15.0, categoryId = 1, unitId = 1),
    Product(productId = 3, name = "Carrot", sku = "SKU003", barcode = "1234567890", purchasePrice = 0.79, salePrice = 1.29, quantity = 20.0, categoryId = 2, unitId = 1),
)
