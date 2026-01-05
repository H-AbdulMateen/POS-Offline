package com.abdulmateen.pos_offline.feature.main.home.presentation.order

import androidx.compose.runtime.Immutable
import com.abdulmateen.pos_offline.core.designsystem.UiText
import com.abdulmateen.pos_offline.domain.models.Product

@Immutable
data class OrderUiState(
    val isLoading: Boolean = false,
    val productList: List<Product> = emptyList(),
    val errorMessage: UiText? = null,
    val searchQuery: String = "",
)