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
    val photoBytes: ByteArray? = null,
    val category: Category?,
    val unit: ItemUnit?,
)

val dummyProducts = listOf(
    Product(productId = 1, name = "Apple", sku = "SKU001", barcode = "1234567890", purchasePrice = 1.99, salePrice = 2.49, quantity = 10.0, category = Category(categoryId = 1, name = "Fruits"), unit = ItemUnit(unitId = 1, name = "kilogram", symbol = "kg")),
    Product(productId = 2, name = "Banana", sku = "SKU002", barcode = "09123", purchasePrice = 0.99, salePrice = 1.49, quantity = 15.0, category = Category(categoryId = 2, name = "Oil"), unit = ItemUnit(unitId = 2, name = "litre", symbol = "l")),
    Product(productId = 3, name = "Carrot", sku = "SKU003", barcode = "1234567890", purchasePrice = 0.79, salePrice = 1.29, quantity = 20.0, category = Category(categoryId = 3, name = "Soap"), unit = ItemUnit(unitId = 3, name = "piece", symbol = "pcs")),
)
