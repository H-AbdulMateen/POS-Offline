package com.abdulmateen.pos_offline.feature.main.home.presentation.order

sealed class ProductListUiAction {
    data class MarkAsFavourite(val productId: Int): ProductListUiAction()
    data class RemoveFromFavourite(val id: Int): ProductListUiAction()
    data object ForceReload: ProductListUiAction()
}