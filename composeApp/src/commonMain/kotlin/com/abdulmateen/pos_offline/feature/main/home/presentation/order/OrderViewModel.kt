package com.abdulmateen.pos_offline.feature.main.home.presentation.order

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState

    fun uiAction(action: ProductListUiAction){

    }



}