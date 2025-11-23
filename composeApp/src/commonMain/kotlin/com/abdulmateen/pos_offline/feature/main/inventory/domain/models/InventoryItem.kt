package com.abdulmateen.pos_offline.feature.main.inventory.domain.models

data class InventoryItem(
    val name: String,
    val sku: String,
    val quantity: Int,
    val location: String
)

val dummyInventory = listOf(
    InventoryItem("Product A", "A001", 100, "Warehouse 1"),
    InventoryItem("Product B", "B002", 200, "Warehouse 2"),
    InventoryItem("Product C", "C003", 150, "Warehouse 1"),
    InventoryItem("Product D", "D004", 50, "Warehouse 3"),
)
