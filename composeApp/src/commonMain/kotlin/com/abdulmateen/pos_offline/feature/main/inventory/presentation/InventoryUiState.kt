package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.compose.ui.graphics.ImageBitmap
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ProductDetail

data class InventoryUiState(
    val isLoading: Boolean = false,
    val searchProduct: String = "",
    val productList: List<Product> = emptyList(),
    val error: String = "",
    val showDialog: Boolean = false,

    val hasNameError: Boolean = false,
    val nameErrorText: String = "",

    val hasSkuError: Boolean = false,
    val skuErrorText: String = "",

    val hasBarcodeError: Boolean = false,
    val barcodeErrorText: String = "",


    val hasPurchasePriceError: Boolean = false,
    val purchasePriceErrorText: String = "",


    val hasSalesPriceError: Boolean = false,
    val salesPriceErrorText: String = "",

    val hasQuantityError: Boolean = false,
    val quantityErrorText: String = "",

    val categoryErrorText: String = "",
    val hasCategoryError: Boolean = false,

    val unitErrorText: String = "",
    val hasUnitError: Boolean = false,


    val imageBitmap: ImageBitmap? = null,
    val imageByteArrayString: String = "",
    val itemImageUrl: String = "",

    val categoryList: List<Category> = emptyList(),
    val unitList: List<ItemUnit> = emptyList(),


    val category: Category? = null,

    val categoryName: String = "",

    val itemUnitName: String = "",
    val itemUnitSymbol: String = "",


    val unit: ItemUnit? = null,


    val itemExpiryDate: String = "",
)
