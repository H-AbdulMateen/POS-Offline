package com.abdulmateen.pos_offline.feature.home.presentation.order

import androidx.compose.runtime.Immutable
import com.abdulmateen.pos_offline.core.designsystem.UiText
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.Customer
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.Product
import org.jetbrains.compose.resources.StringResource

@Immutable
data class OrderUiState(
    val isLoading: Boolean = false,
    val productList: List<Product> = emptyList(),
    val cartItems: List<CartItem> = emptyList(),
    val cartItemCount: Int = 0,
    val errorMessage: UiText? = null,
    val searchQuery: String = "",
    val subTotal: Double = 0.0,
    val categoryList: List<Category> = emptyList(),
    val unitList: List<ItemUnit> = emptyList(),
    val isDeleteDialogVisible: Boolean = false,
    val discountField: String = "",
    val discountFieldErrorMessage: StringResource? = null,
    val hasDiscountError: Boolean = false,
    val taxField: String = "",
    val taxFieldErrorMessage: StringResource? = null,
    val hasTaxError: Boolean = false,
    val discount: Double = 0.0,
    val tax: Double = 0.0,
    val isDiscountDialogVisible: Boolean = false,
    val isTaxDialogVisible: Boolean = false,
    val businessName: String = "",
    val currencySymbol: String = "$",
    val customerList: List<Customer> = emptyList(),
    val selectedCustomer: Customer? = null
) {
    val total: Double get() = subTotal - discount + tax
}
