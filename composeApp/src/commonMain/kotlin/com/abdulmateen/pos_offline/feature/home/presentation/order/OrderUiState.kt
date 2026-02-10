package com.abdulmateen.pos_offline.feature.home.presentation.order

import androidx.compose.runtime.Immutable
import com.abdulmateen.pos_offline.core.designsystem.UiText
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.Product

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
)