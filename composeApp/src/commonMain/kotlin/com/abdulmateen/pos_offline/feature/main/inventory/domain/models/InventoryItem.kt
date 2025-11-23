package com.abdulmateen.pos_offline.feature.main.inventory.domain.models

data class InventoryItem(
    val name: String,
    val description: String? = null,
    val sku: String,
    val quantity: Int,
    val salesPrice: Double,
    val purchasePrice: Double,
    val imageUrl: String? = null,

)

val dummyInventory = listOf(
    InventoryItem(name = "Product A", sku = "A001", quantity = 100, salesPrice = 100.0, purchasePrice = 95.0),
    InventoryItem(name = "Product B", sku = "B002", quantity =  200, salesPrice = 200.0, purchasePrice = 190.0),
    InventoryItem(name = "Product C", sku = "C003", quantity = 150, salesPrice =  300.0, purchasePrice = 285.0),
    InventoryItem(name = "Product D", sku = "D004", quantity = 50, salesPrice =  400.0, purchasePrice = 380.0),
)
