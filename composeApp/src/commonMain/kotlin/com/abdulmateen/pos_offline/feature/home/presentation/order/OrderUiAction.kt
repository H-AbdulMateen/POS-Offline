package com.abdulmateen.pos_offline.feature.home.presentation.order

import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.Customer

sealed class OrderUiAction {
    data class OnSearchProduct(val query: String) : OrderUiAction()
    data class AddProductToCart(val product: Product) : OrderUiAction()
    object ClearCart : OrderUiAction()
    data class IncrementInQuantity(val productId: Long): OrderUiAction()
    data class DecrementInQuantity(val productId: Long): OrderUiAction()
    data class ToggleDeleteDialog(val productId: Long? = null): OrderUiAction()
    object ToggleDiscountDialog: OrderUiAction()
    object ToggleTaxDialog: OrderUiAction()
    data class UpdateDiscountField(val discount: String): OrderUiAction()
    data object ApplyDiscount: OrderUiAction()
    data object ApplyTax: OrderUiAction()
    data class UpdateTaxField(val tax: String): OrderUiAction()
    object RemoveCartItem: OrderUiAction()
    data class Checkout(val customerName: String?, val customerPhone: String?): OrderUiAction()
    data class CreateCredit(val customerName: String, val phoneNumber: String?, val paidAmount: Double): OrderUiAction()
    data class SelectCustomer(val customer: Customer?): OrderUiAction()
    data class UpdateCartItemPrice(val productId: Long, val newPrice: Double): OrderUiAction()
    data class LoadOrderForUpdate(val orderId: Long): OrderUiAction()
}
