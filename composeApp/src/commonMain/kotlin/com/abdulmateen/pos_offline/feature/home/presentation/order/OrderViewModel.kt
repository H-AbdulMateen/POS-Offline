package com.abdulmateen.pos_offline.feature.home.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import com.abdulmateen.pos_offline.domain.use_cases.CartUseCases
import com.abdulmateen.pos_offline.domain.use_cases.ProductUseCases
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
    private val productUseCases: ProductUseCases,
    private val cartUseCases: CartUseCases,
    private val repository: InventoryRepository //TODO: Remove this before go to release
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState
        .onStart {
            loadCategories() //TODO: Remove this before go to release
            loadUnits() //TODO: Remove this before go to release
            getCartItemsCount()
            loadCartItems()
            calculateSubTotal()
            if (uiState.value.categoryList.isNotEmpty() && uiState.value.unitList.isNotEmpty()) {
                loadProducts()
            }

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = OrderUiState()
        )
    var selectedProductId: Long = 0

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
                    cartUseCases.removeItem(selectedProductId)
                    _uiState.update {
                        it.copy(
                            isDeleteDialogVisible = !uiState.value.isDeleteDialogVisible
                        )
                    }
                }
            }

            is OrderUiAction.ToggleDeleteDialog -> {
                selectedProductId = action.productId ?: 0
                _uiState.update {
                    it.copy(
                        isDeleteDialogVisible = !uiState.value.isDeleteDialogVisible
                    )
                }
            }
        }
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartUseCases.getCartItemList()
                .onEach {items ->
                    _uiState.update { state ->
                        state.copy(
                            cartItems = items
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

    private fun calculateSubTotal() {
        viewModelScope.launch {

            cartUseCases.calculateSubTotal().collect {total ->
                _uiState.update {
                    it.copy(
                        subTotal = total
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
                    imagePath = item.imagePath,
                    sku = item.sku,
                    quantity = 1.0,
                    price = item.price,
                    discount = 0.0,
                    unitPrice = item.price * 1.0
                )
            )
        }
    }

    init {
        viewModelScope.launch {
            _searchProductField.debounce(600)
                .distinctUntilChanged()
                .mapLatest { query ->
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    productUseCases.searchProduct(query)
                }
                .collect { filteredList ->
                    filteredList.onEach { list ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                productList = list
                            )
                        }
                    }.launchIn(viewModelScope)
                }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            productUseCases.getProductList()
                .onEach { products ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            productList = products
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().onEach { categories ->
                _uiState.update {
                    it.copy(
                        categoryList = categories
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun loadUnits(){
        viewModelScope.launch {
            repository.getAllUnits().onEach { units ->
                _uiState.update {
                    it.copy(
                        unitList = units
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

}