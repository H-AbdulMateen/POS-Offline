package com.abdulmateen.pos_offline.feature.main.home.domain.models

data class Product(
    val productId: Long = 0,
    val name: String,
    val sku: String,
    val price: Double,
    val quantity: Double,
    val photoBytes: ByteArray? = null,
    val unit: ItemUnit? = null,
    )



val dummyProducts = listOf(
    Product(productId = 1, name = "Apple", sku = "SKU123", price = 2.49, quantity = 10.0, unit = ItemUnit(unitId = 1, name = "kilogram", symbol = "kg")),
    Product(productId = 2, name = "Banana", sku = "SKU456",  price = 1.49, quantity = 15.0, unit = ItemUnit(unitId = 2, name = "litre", symbol = "l")),
    Product(productId = 3, name = "Carrot", sku = "SKU789", price = 1.29, quantity = 20.0, unit = ItemUnit(unitId = 3, name = "piece", symbol = "pcs")),
)
