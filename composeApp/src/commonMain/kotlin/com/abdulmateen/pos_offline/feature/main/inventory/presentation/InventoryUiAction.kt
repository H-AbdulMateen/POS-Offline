package com.abdulmateen.pos_offline.feature.main.inventory.presentation

sealed interface InventoryUiAction {
    data class UpdateItemName(val name: String): InventoryUiAction
    data class UpdateItemDescription(val description: String): InventoryUiAction
    data class UpdateItemSku(val sku: String): InventoryUiAction
    data class UpdateItemQuantity(val quantity: String): InventoryUiAction
    data class UpdateItemSalesPrice(val salesPrice: String): InventoryUiAction
    data class UpdateItemPurchasePrice(val purchasePrice: String): InventoryUiAction
    data class UpdateItemImageUrl(val imageUrl: String): InventoryUiAction
    data class UpdateCategory(val category: String): InventoryUiAction
    data class UpdateSubCategory(val subCategory: String): InventoryUiAction
    data class UpdateBrand(val brand: String): InventoryUiAction
    data class UpdateColor(val color: String): InventoryUiAction

    object AddItem: InventoryUiAction
    object UpdateItem: InventoryUiAction
    object DeleteItem: InventoryUiAction
    object ClearForm: InventoryUiAction

}