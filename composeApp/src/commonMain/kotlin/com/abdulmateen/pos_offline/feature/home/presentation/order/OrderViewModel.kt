package com.abdulmateen.pos_offline.feature.home.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.repository.CreditRepository
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
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
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.field_required

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class OrderViewModel(
    private val productUseCases: ProductUseCases,
    private val cartUseCases: CartUseCases,
    private val dataStoreManager: DataStoreManager,
    private val repository: InventoryRepository, //TODO: Remove this before go to release
    private val creditRepository: CreditRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState
        .onStart {
            loadCategories() //TODO: Remove this before go to release
            loadUnits() //TODO: Remove this before go to release
            getCartItemsCount()
            loadCartItems()
            calculateSubTotal()
            loadSettings()
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

            is OrderUiAction.Checkout -> {
                checkout(action.customerName, action.customerPhone)
            }
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
            is OrderUiAction.UpdateDiscountField -> {
                _uiState.update {
                    it.copy(
                        discountField = action.discount
                    )
                }
            }
            is OrderUiAction.UpdateTaxField -> {
                _uiState.update {
                    it.copy(
                        taxField = action.tax
                    )
                }
            }
            is OrderUiAction.ApplyDiscount -> {
                applyDiscount()
            }
            is OrderUiAction.ApplyTax -> {
                applyTax()
            }

            is OrderUiAction.ToggleDeleteDialog -> {
                selectedProductId = action.productId ?: 0
                _uiState.update {
                    it.copy(
                        isDeleteDialogVisible = !uiState.value.isDeleteDialogVisible
                    )
                }
            }
            OrderUiAction.ToggleDiscountDialog -> {
                _uiState.update {
                    it.copy(
                        isDiscountDialogVisible = !uiState.value.isDiscountDialogVisible
                    )
                }
            }
            OrderUiAction.ToggleTaxDialog -> {
                _uiState.update {
                    it.copy(
                        isTaxDialogVisible = !uiState.value.isTaxDialogVisible
                    )
                }
            }
            is OrderUiAction.CreateCredit -> {
                createCredit(action.customerName, action.phoneNumber, action.paidAmount)
            }
        }
    }

    private fun createCredit(customerName: String, phoneNumber: String?, paidAmount: Double) {
        viewModelScope.launch {
            val total = uiState.value.total
            creditRepository.upsertCredit(
                com.abdulmateen.pos_offline.data.database.entities.CreditEntity(
                    customerName = customerName,
                    phoneNumber = phoneNumber,
                    totalAmount = total,
                    paidAmount = paidAmount,
                    remainingAmount = total - paidAmount
                )
            )
            orderRepository.checkout(
                cartItems = uiState.value.cartItems,
                subTotal = uiState.value.subTotal,
                discount = uiState.value.discount,
                tax = uiState.value.tax,
                total = uiState.value.total,
                customerName = customerName,
                customerPhone = phoneNumber,
                paymentMethod = "CREDIT"
            )
            cartUseCases.clearCartItems()
        }
    }

    private fun checkout(customerName: String?, customerPhone: String?) {
        viewModelScope.launch {
            orderRepository.checkout(
                cartItems = uiState.value.cartItems,
                subTotal = uiState.value.subTotal,
                discount = uiState.value.discount,
                tax = uiState.value.tax,
                total = uiState.value.total,
                customerName = customerName,
                customerPhone = customerPhone,
                paymentMethod = "CASH" // Defaulting to CASH for now, can be improved if needed
            )
            cartUseCases.clearCartItems()
        }
    }

    private fun applyDiscount() {
        if (uiState.value.discountField.isEmpty()){
            _uiState.update {
                it.copy(
                    discountFieldErrorMessage = Res.string.field_required,
                    hasDiscountError = true
                )
            }
            return
        }
        val discount = uiState.value.discountField.toDouble()
        viewModelScope.launch {
            dataStoreManager.setDoubleValue(PrefKeys.DISCOUNT, discount)
        }
        _uiState.update {
            it.copy(
                discount = discount,
                discountField = "",
                isDiscountDialogVisible = false,
                hasDiscountError = false,
                discountFieldErrorMessage = null
            )
        }
    }

    private fun applyTax() {
        if (uiState.value.taxField.isEmpty()){
            _uiState.update {
                it.copy(
                    taxFieldErrorMessage = Res.string.field_required,
                    hasTaxError = true
                )
            }
            return
        }
        val tax = uiState.value.taxField.toDouble()
        viewModelScope.launch {
            dataStoreManager.setDoubleValue(PrefKeys.TAX,tax)
        }
        _uiState.update {
            it.copy(
                tax = tax,
                taxField = "",
                isTaxDialogVisible = false,
                hasTaxError = false,
                taxFieldErrorMessage = null
            )
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

    private fun loadSettings() {
        viewModelScope.launch {
            val businessName = dataStoreManager.getStringValue(PrefKeys.BUSINESS_NAME)
            val currencySymbol = dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL)
            _uiState.update {
                it.copy(
                    businessName = businessName,
                    currencySymbol = currencySymbol.ifEmpty { "$" }
                )
            }
        }
    }

}