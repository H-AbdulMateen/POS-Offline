package com.abdulmateen.pos_offline.feature.main.home.presentation.order

import androidx.compose.runtime.Immutable
import com.abdulmateen.pos_offline.core.designsystem.UiText

@Immutable
data class ProductListUiState(
    val isLoading: Boolean = false,
    val productList: List<Product> = emptyList(),
    val errorMessage: UiText? = null,
    val favouriteProducts: List<Product> = emptyList(),
    val isRefreshing: Boolean = false
)