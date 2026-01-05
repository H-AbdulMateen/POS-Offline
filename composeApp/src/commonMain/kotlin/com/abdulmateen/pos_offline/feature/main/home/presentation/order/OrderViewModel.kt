package com.abdulmateen.pos_offline.feature.main.home.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.use_cases.product.ProductUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class OrderViewModel(
    private val productUseCases: ProductUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState
        .onStart {
            loadProducts()
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = OrderUiState()
    )

    private val _searchProductField = MutableStateFlow("")
    val searchProductField: StateFlow<String> = _searchProductField.asStateFlow()


    fun uiAction(action: OrderUiAction){
        when(action){
            is OrderUiAction.OnSearchProduct -> {
                _searchProductField.value = action.query
            }
            is OrderUiAction.AddProductToCart -> {
                viewModelScope.launch {

                }
            }
            is OrderUiAction.RemoveProductFromCart -> {

            }
            is OrderUiAction.ClearCart -> {

            }
        }
    }

    init {
        viewModelScope.launch {
            _searchProductField.debounce(600)
                .distinctUntilChanged()
                .mapLatest { query ->
                    productUseCases.searchProduct(query)
                }
                .collect { filteredList ->
                    filteredList.onEach { list ->
                        _uiState.update {
                            it.copy(
                                productList = list
                            )
                        }
                    }.launchIn(viewModelScope)
                }
        }
    }

    private fun loadProducts(){
        viewModelScope.launch {
            productUseCases.getProductList()
                .onEach {products ->
                    _uiState.update {
                        it.copy(
                            productList = products
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }



}