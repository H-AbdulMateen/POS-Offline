package com.abdulmateen.pos_offline.feature.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.use_cases.CartUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainViewModel(
    private val cartUseCases: CartUseCases,
): ViewModel() {
    val cartCount = mutableStateOf(0)
    init {
        viewModelScope.launch {
            cartUseCases.getCartItemCount().collect {
             cartCount.value = it
            }
        }
    }
}