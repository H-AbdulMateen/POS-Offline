package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.compose.ui.graphics.ImageBitmap
import com.abdulmateen.pos_offline.core.designsystem.UiText
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.Product

data class InventoryUiState(
    val isLoading: Boolean = false,
    val searchProductQuery: String = "",
    val productList: List<Product> = emptyList(),
    val errorResult: UiText? = null,
    val categoryErrorResult: UiText? = null,
    val unitErrorResult: UiText? = null,

    val showDialog: Boolean = false,
    val categoryDialogVisible: Boolean = false,
    val unitDialogVisible: Boolean = false,


    val name: String = "",
    val hasNameError: Boolean = false,
    val nameErrorText: String = "",


    val sku: String = "",
    val hasSkuError: Boolean = false,
    val skuErrorText: String = "",

    val barcode: String = "",
    val hasBarcodeError: Boolean = false,
    val barcodeErrorText: String = "",


    val purchasePrice: String = "",
    val hasPurchasePriceError: Boolean = false,
    val purchasePriceErrorText: String = "",

    val salePrice: String = "",
    val hasSalesPriceError: Boolean = false,
    val salesPriceErrorText: String = "",

    val stock: String = "",
    val hasStockError: Boolean = false,
    val stockErrorText: String = "",

    val categoryErrorText: String = "",
    val hasCategoryError: Boolean = false,

    val unitErrorText: String = "",
    val hasUnitError: Boolean = false,


    val imageBitmap: ImageBitmap? = null,
    val photoBytes: ByteArray? = null,
    val itemImageUrl: String = "",

    val categoryList: List<Category> = emptyList(),
    val unitList: List<ItemUnit> = emptyList(),


    val category: Category? = null,

    val categoryName: String = "",
    val hasCategoryNameError: Boolean = false,
    val categoryNameErrorText: String = "",


    val itemUnitName: String = "",
    val hasItemUnitNameError: Boolean = false,
    val itemUnitNameErrorText: String = "",
    val itemUnitSymbol: String = "",
    val hasItemUnitSymbolError: Boolean = false,
    val itemUnitSymbolErrorText: String = "",


    val unit: ItemUnit? = null,


    val itemExpiryDate: String = "",
)
