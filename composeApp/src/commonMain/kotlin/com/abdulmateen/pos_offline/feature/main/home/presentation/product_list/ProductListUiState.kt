package com.abdulmateen.pos_offline.feature.main.home.presentation.product_list

import androidx.compose.runtime.Immutable
import com.abdulmateen.pos_offline.core.designsystem.UiText
import com.abdulmateen.pos_offline.feature.main.home.domain.Product

@Immutable
data class ProductListUiState(
    val isLoading: Boolean = false,
    val productList: List<Product> = emptyList(),
    val errorMessage: UiText? = null,
    val favouriteProducts: List<Product> = emptyList(),
    val isRefreshing: Boolean = false
)