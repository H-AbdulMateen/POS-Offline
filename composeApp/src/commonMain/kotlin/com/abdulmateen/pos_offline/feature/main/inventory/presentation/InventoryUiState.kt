package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.compose.ui.graphics.ImageBitmap
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product

data class InventoryUiState(
    val isLoading: Boolean = false,
    val searchProduct: String = "",

    val productList: List<Product> = emptyList(),
    val error: String = "",
    val showDialog: Boolean = false,

    val name: String = "",
    val hasNameError: Boolean = false,
    val nameErrorText: String = "",

    val description: String = "",
    val sku: String = "",
    val hasSkuError: Boolean = false,
    val skuErrorText: String = "",

    val barcode: String = "",
    val hasBarcodeError: Boolean = false,
    val barcodeErrorText: String = "",


    val purchasePrice: String = "",
    val hasPurchasePriceError: Boolean = false,
    val purchasePriceErrorText: String = "",


    val salesPrice: String = "",
    val hasSalesPriceError: Boolean = false,
    val salesPriceErrorText: String = "",

    val quantity: String = "",
    val hasQuantityError: Boolean = false,
    val quantityErrorText: String = "",

    val imageBitmap: ImageBitmap? = null,
    val imageByteArrayString: String = "",
    val itemImageUrl: String = "",

    val category: Category? = null,

    val unit: ItemUnit? = null,


    val itemExpiryDate: String = "",
)
