package com.abdulmateen.pos_offline.feature.main.home.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.use_cases.cart.CartUseCases
import com.abdulmateen.pos_offline.domain.use_cases.product.ProductUseCases
import com.abdulmateen.pos_offline.feature.main.home.presentation.components.CartListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
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
    private val productUseCases: ProductUseCases,
    private val cartUseCases: CartUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState
        .onStart {
            getCartItemsCount()
            loadProducts()
            loadCartItems()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = OrderUiState()
        )

    private val _searchProductField = MutableStateFlow("")
    val searchProductField: StateFlow<String> = _searchProductField.asStateFlow()


    fun uiAction(action: OrderUiAction) {
        when (action) {
            is OrderUiAction.OnSearchProduct -> {
                _searchProductField.value = action.query
            }

            is OrderUiAction.AddProductToCart -> {
                addToCart(item = action.product)
            }

            is OrderUiAction.RemoveProductFromCart -> {
                viewModelScope.launch {
                    cartUseCases.removeItem(productId = action.product.productId)
                }
            }

            is OrderUiAction.ClearCart -> {
                viewModelScope.launch {
                    cartUseCases.clearCartItems()
                }
            }

            OrderUiAction.Checkout -> {}
            is OrderUiAction.DecrementInQuantity -> {
                viewModelScope.launch {
                    cartUseCases.decrementInQuantity(productId = action.productId)
                }
            }
            is OrderUiAction.IncrementInQuantity -> {
                viewModelScope.launch {
                    cartUseCases.incrementInQuantity(productId = action.productId)
                }
            }
            is OrderUiAction.RemoveCartItem -> {
                viewModelScope.launch {
                    cartUseCases.removeItem(productId = action.productId)
                }
            }
        }
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartUseCases.getCartItemList()
                .onEach {
                    _uiState.update { state ->
                        state.copy(
                            cartItems = it
                        )
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun getCartItemsCount() {
        viewModelScope.launch {
            cartUseCases.getCartItemCount().collect { count ->
                _uiState.update {
                    it.copy(
                        cartItemCount = count
                    )
                }
            }
        }
    }

    private fun addToCart(item: Product) {
        viewModelScope.launch {
            cartUseCases.addItemToCart(
                item = CartItem(
                    productId = item.productId,
                    productName = item.name,
                    sku = item.sku,
                    quantity = 1.0,
                    price = item.price,
                    discount = 0.0,
                )
            )
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

    private fun loadProducts() {
        viewModelScope.launch {
            productUseCases.getProductList()
                .onEach { products ->
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