package com.abdulmateen.pos_offline.feature.inventory.presentation

import androidx.compose.ui.graphics.ImageBitmap
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.inventory.presentation.models.ProductUi

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
    data class ToggleAddEditProductDialog(val productId: Long?) : InventoryUiAction
    data class ToggleDetailDialog(val productId: Long?) : InventoryUiAction

    object OnAddNewCategory: InventoryUiAction
    object OnAddNewUnit: InventoryUiAction


    data class OnAddItemClick(val isEditing: Boolean) : InventoryUiAction

    data class OnEditItemClick(val productId: Long?) : InventoryUiAction
    object OnDeleteItemClick : InventoryUiAction
    object ToggleCategoryDialog : InventoryUiAction
    object ToggleUnitDialog : InventoryUiAction
    

    object ClearForm: InventoryUiAction
    data class ToggleOptionReveal(val productId: Long?, val isRevealed: Boolean) : InventoryUiAction
    data class ToggleDeleteDialog(val productId: Long?) : InventoryUiAction

}