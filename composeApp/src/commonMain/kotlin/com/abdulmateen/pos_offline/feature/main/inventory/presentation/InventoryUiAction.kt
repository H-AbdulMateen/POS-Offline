package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
sealed interface InventoryUiAction {

    data class OnSearchProductChange(val searchProduct: String) : InventoryUiAction
    data class OnNameChange(val name: String) : InventoryUiAction
    data class OnDescriptionChange(val description: String) : InventoryUiAction
    data class OnSkuChange(val sku: String) : InventoryUiAction
    data class OnBarcodeChange(val barcode: String) : InventoryUiAction
    data class OnPurchasePriceChange(val purchasePrice: String) : InventoryUiAction
    data class OnSalesPriceChange(val salesPrice: String) : InventoryUiAction
    data class OnQuantityChange(val quantity: String) : InventoryUiAction
    class OnImageUrlChange(val bytes: ByteArray?) : InventoryUiAction
    data class OnExpiryDateChange(val expiryDate: String) : InventoryUiAction
    data class OnCategoryChange(val category: Category) : InventoryUiAction

    object OnAddItemClick : InventoryUiAction

    data class OnEditItemClick(val product: Product) : InventoryUiAction
    data class OnDeleteItemClick(val product: Product) : InventoryUiAction

    object ClearForm: InventoryUiAction

}