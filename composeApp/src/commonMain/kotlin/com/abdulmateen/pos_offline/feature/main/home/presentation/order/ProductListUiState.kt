package com.abdulmateen.pos_offline.feature.main.home.presentation.order

import androidx.compose.runtime.Immutable
import com.abdulmateen.pos_offline.core.designsystem.UiText
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product

@Immutable
data class ProductListUiState(
    val isLoading: Boolean = false,
    val productList: List<Product> = emptyList(),
    val errorMessage: UiText? = null,
    val searchQuery: String = "",
)