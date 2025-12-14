package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product

data class InventoryUiState(
    val isLoading: Boolean = false,
    val items: List<Product> = emptyList(),
    val error: String = "",
    val showDialog: Boolean = false,

    val itemName: String = "",
    val hasItemNameError: Boolean = false,
    val itemNameError: String = "",

    val itemDescription: String = "",
    val itemSku: String = "",
    val itemQuantity: String = "",

    val itemSalesPrice: String = "",
    val hasItemSalesPriceError: Boolean = false,
    val itemSalesPriceError: String = "",

    val itemPurchasePrice: String = "",
    val hasItemPurchasePriceError: Boolean = false,
    val itemPurchasePriceError: String = "",

    val itemExpiryDate: String = "",
    val category: String = "",
    val subCategory: String = "",
    val itemImageUrl: String = "",
    val itemBarcode: String = "",
)
