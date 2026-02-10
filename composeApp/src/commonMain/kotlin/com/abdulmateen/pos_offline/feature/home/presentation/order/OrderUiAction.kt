package com.abdulmateen.pos_offline.feature.home.presentation.order

import com.abdulmateen.pos_offline.domain.models.Product

sealed class OrderUiAction {
    data class OnSearchProduct(val query: String) : OrderUiAction()
    data class AddProductToCart(val product: Product) : OrderUiAction()
    object ClearCart : OrderUiAction()

    data class IncrementInQuantity(val productId: Long): OrderUiAction()
    data class DecrementInQuantity(val productId: Long): OrderUiAction()
    data class ToggleDeleteDialog(val productId: Long? = null): OrderUiAction()
    object RemoveCartItem: OrderUiAction()
    object Checkout: OrderUiAction()
}