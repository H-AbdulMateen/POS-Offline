package com.abdulmateen.cmpskeleton.feature.main.home.presentation.product_list

sealed class ProductListUiAction {
    data class MarkAsFavourite(val productId: Int): ProductListUiAction()
    data class RemoveFromFavourite(val id: Int): ProductListUiAction()
    data object ForceReload: ProductListUiAction()
}