package com.abdulmateen.pos_offline.feature.main.home.presentation.order

import com.abdulmateen.pos_offline.domain.models.Product

sealed class OrderUiAction {
    data class OnSearchProduct(val query: String) : OrderUiAction()
    data class AddProductToCart(val product: Product) : OrderUiAction()
    data class RemoveProductFromCart(val product: Product) : OrderUiAction()
    object ClearCart : OrderUiAction()
}