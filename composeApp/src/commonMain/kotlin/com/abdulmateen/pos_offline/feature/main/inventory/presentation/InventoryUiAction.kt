package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.compose.ui.graphics.ImageBitmap
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product

sealed interface InventoryUiAction {

    data class OnSearchProductChange(val searchProduct: String) : InventoryUiAction
    data class OnNameChange(val name: String) : InventoryUiAction
    data class OnSkuChange(val sku: String) : InventoryUiAction
    data class OnBarcodeChange(val barcode: String) : InventoryUiAction
    data class OnPurchasePriceChange(val purchasePrice: String) : InventoryUiAction
    data class OnSalesPriceChange(val salesPrice: String) : InventoryUiAction
    data class OnStockChange(val quantity: String) : InventoryUiAction
    class OnImageSelection(val imageBitmap: ImageBitmap, val bytes: ByteArray?) : InventoryUiAction
    data class OnExpiryDateChange(val expiryDate: String) : InventoryUiAction
    data class OnCategoryChange(val category: Category) : InventoryUiAction
    data class OnItemUnitChange(val itemUnit: ItemUnit) : InventoryUiAction
    data class OnCategoryNameChange(val categoryName: String) : InventoryUiAction
    data class OnUnitNameChange(val unitName: String) : InventoryUiAction
    data class OnUnitSymbolChange(val unitSymbol: String) : InventoryUiAction
    object OnAddNewCategory: InventoryUiAction
    object OnAddNewUnit: InventoryUiAction


    object OnAddItemClick : InventoryUiAction

    data class OnEditItemClick(val productId: Long) : InventoryUiAction
    data class OnDeleteItemClick(val product: Product) : InventoryUiAction
    object ToggleCategoryDialog : InventoryUiAction
    object ToggleUnitDialog : InventoryUiAction

    object ClearForm: InventoryUiAction

}