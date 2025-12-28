package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.common.presentation.utils.Patterns
import com.abdulmateen.pos_offline.core.domain.onError
import com.abdulmateen.pos_offline.core.domain.onSuccess
import com.abdulmateen.pos_offline.feature.main.home.domain.InventoryRepository
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ProductDetail
import com.abdulmateen.pos_offline.utils.Validator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class InventoryViewModel constructor(
    private val repository: InventoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.onStart {
        loadProducts()
        loadCategories()
        loadUnits()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InventoryUiState()
    )
    private val _eventChannel = Channel<InventoryEvents>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _searchProductQuery = MutableStateFlow("")
    val searchProductQuery: StateFlow<String> = _searchProductQuery.asStateFlow()
    init {
        viewModelScope.launch {
            _searchProductQuery.debounce(600)
                .distinctUntilChanged()
                .mapLatest {query ->
                    repository.searchProductsByName(query)
                }
                .collect {filteredList ->
                    filteredList.onEach {list ->
                        _uiState.update {
                            it.copy(
                                productList = list
                            )
                        }
                    }.launchIn(viewModelScope)
                }
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

    fun onSearchProductQueryChange(query: String) {
        _searchProductQuery.value = query
    }


    fun uiAction(action: InventoryUiAction) {
        when (action) {
            InventoryUiAction.ClearForm -> {
                clearForm()
            }

            InventoryUiAction.OnAddItemClick -> {
                    validateFields()
            }

            is InventoryUiAction.OnBarcodeChange -> {
                _uiState.update {
                    it.copy(
                        barcode = action.barcode
                    )
                }

            }

            is InventoryUiAction.OnCategoryChange -> {
                _uiState.update {
                    it.copy(
                        category = action.category
                    )
                }
            }

            is InventoryUiAction.OnDeleteItemClick -> {
                deleteProduct(action.product)
            }

            is InventoryUiAction.OnEditItemClick -> {
                viewModelScope.launch {
                    repository.getProductById(action.productId)?.let {
                        _uiState.update { it }
                    }
                }
            }

            is InventoryUiAction.OnExpiryDateChange -> {
                _uiState.update {
                    it.copy(
                        itemExpiryDate = action.expiryDate
                    )
                }
            }

            is InventoryUiAction.OnImageSelection -> {
                _uiState.update {
                    it.copy(
                        imageBitmap = action.imageBitmap,
                        photoBytes = action.bytes

                    )
                }
            }

            is InventoryUiAction.OnNameChange -> {
                _uiState.update {
                    it.copy(
                        name = action.name
                    )
                }
            }

            is InventoryUiAction.OnPurchasePriceChange -> {
                val inputPurchasePrice = action.purchasePrice
                if (inputPurchasePrice.isEmpty() || inputPurchasePrice.matches(Patterns.decimalRegex)){
                    _uiState.update {
                        it.copy(
                            purchasePrice = inputPurchasePrice,
                            hasPurchasePriceError = false,
                            purchasePriceErrorText = ""
                        )
                    }
                }else{
                    _uiState.update {
                        it.copy(
                            hasPurchasePriceError = true,
                            purchasePriceErrorText = "Only decimals are allowed"
                        )
                    }
                }
            }

            is InventoryUiAction.OnStockChange -> {
                val inputQuantity = action.quantity

                if (inputQuantity.isEmpty() || inputQuantity.matches(Patterns.decimalRegex)){
                    _uiState.update {
                        it.copy(
                            stock = inputQuantity,
                            hasStockError = false,
                            stockErrorText = ""
                        )
                    }
                }else{
                    _uiState.update {
                        it.copy(
                            hasStockError = true,
                            stockErrorText = "Only decimals are allowed"
                        )
                    }
                }
            }

            is InventoryUiAction.OnSalesPriceChange -> {
                val inputPrice = action.salesPrice
                if (inputPrice.isEmpty() || inputPrice.matches(Patterns.decimalRegex)) {
                    _uiState.update {
                        it.copy(
                            salePrice = inputPrice,
                            hasSalesPriceError = false,
                            salesPriceErrorText = ""
                        )
                    }
                }else{
                    _uiState.update {
                        it.copy(
                            hasSalesPriceError = true,
                            salesPriceErrorText = "Only decimals are allowed"
                        )
                    }
                }
            }

            is InventoryUiAction.OnSearchProductChange -> {
                _uiState.update {
                    it.copy(
                        searchProductQuery = action.searchProduct
                    )
                }
            }

            is InventoryUiAction.OnSkuChange -> {
                _uiState.update {
                    it.copy(
                        sku = action.sku
                    )
                }
            }

            is InventoryUiAction.OnItemUnitChange -> {
                _uiState.update {
                    it.copy(
                        unit = action.itemUnit
                    )
                }
            }

            is InventoryUiAction.OnCategoryNameChange -> {
                _uiState.update {
                    it.copy(
                        categoryName = action.categoryName
                    )
                }
            }

            is InventoryUiAction.OnUnitNameChange -> {
                _uiState.update {
                    it.copy(
                        itemUnitName = action.unitName
                    )
                }
            }

            is InventoryUiAction.OnUnitSymbolChange -> {
                _uiState.update {
                    it.copy(
                        itemUnitSymbol = action.unitSymbol
                    )
                }
            }

            InventoryUiAction.OnAddNewCategory -> {
                addNewCategory()
            }

            InventoryUiAction.OnAddNewUnit -> {
                addNewUnit()
            }


        }
    }

    private fun deleteProduct(item: Product) {
        viewModelScope.launch {
            repository.deleteProduct(item.productId)
            _uiState.update {
                it.copy(
                    productList = it.productList.filter { product -> product != item }
                )
            }
        }
    }

    private fun addNewUnit() {
        viewModelScope.launch {
            repository.insertUnit(
                unit = ItemUnit(
                    name = uiState.value.itemUnitName,
                    symbol = uiState.value.itemUnitSymbol
                )
            )
            _uiState.update {
                it.copy(
                    itemUnitName = "",
                    itemUnitSymbol = ""
                )
            }
        }
    }

    private fun addNewCategory() {
        viewModelScope.launch {
            repository.insertCategory(
                Category(
                    name = uiState.value.categoryName
                )
            )
            _uiState.update {
                it.copy(
                    categoryName = ""
                )
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            repository.getAllProducts().onEach { products ->
                _uiState.update {
                    it.copy(
                        productList = products
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun clearForm() {
        _uiState.update {
            it.copy(
                name = "",
                sku = "",
                barcode = "",
                purchasePrice = "",
                salePrice = "",
                stock = "",
                imageBitmap = null,
                photoBytes = null,
                category = null,
                unit = null,
                itemExpiryDate = ""
            )
        }
    }

    private fun validateFields() {
        viewModelScope.launch {
            val validateName = Validator.validateNonEmpty(uiState.value.name)
            if (!validateName.isValid) {
                _uiState.update {
                    it.copy(
                        hasNameError = validateName.isValid,
                        nameErrorText = validateName.errorMessage
                    )
                }
                return@launch
            }
            val validateSku = Validator.validateNonEmpty(uiState.value.sku)
            if (!validateSku.isValid) {
                _uiState.update {
                    it.copy(
                        hasSkuError = validateSku.isValid,
                        skuErrorText = validateSku.errorMessage
                    )
                }
                return@launch
            }
            val validateBarcode = Validator.validateNonEmpty(uiState.value.barcode)
            if (!validateBarcode.isValid) {
                _uiState.update {
                    it.copy(
                        hasBarcodeError = validateBarcode.isValid,
                        barcodeErrorText = validateBarcode.errorMessage
                    )
                }
                return@launch
            }
            val validatePurchasePrice =
                Validator.validateNonEmpty(uiState.value.purchasePrice)
            if (!validatePurchasePrice.isValid) {
                _uiState.update {
                    it.copy(
                        hasPurchasePriceError = validatePurchasePrice.isValid,
                        purchasePriceErrorText = validatePurchasePrice.errorMessage
                    )
                }
                return@launch
            }

            val validateSalesPrice = Validator.validateNonEmpty(uiState.value.salePrice)
            if (!validateSalesPrice.isValid) {
                _uiState.update {
                    it.copy(
                        hasSalesPriceError = validateSalesPrice.isValid,
                        salesPriceErrorText = validateSalesPrice.errorMessage
                    )
                }
                return@launch
            }

            val validateQuantity = Validator.validateNonEmpty(uiState.value.stock)
            if (!validateQuantity.isValid) {
                _uiState.update {
                    it.copy(
                        hasStockError = validateQuantity.isValid,
                        stockErrorText = validateQuantity.errorMessage
                    )
                }
                return@launch
            }

            val validateCategory = Validator.validateNonEmpty(uiState.value.category?.name!!)
            if (!validateCategory.isValid) {
                _uiState.update {
                    it.copy(
                        hasCategoryError = validateCategory.isValid,
                        categoryErrorText = validateCategory.errorMessage
                    )
                }
                return@launch
            }

            val validateUnit = Validator.validateNonEmpty(uiState.value.unit?.name!!)
            if (!validateUnit.isValid) {
                _uiState.update {
                    it.copy(
                        hasUnitError = validateUnit.isValid,
                        unitErrorText = validateUnit.errorMessage
                    )
                }
                return@launch
            }

            repository.insertProduct(
                ProductDetail(
                    name = uiState.value.name,
                    sku = uiState.value.sku,
                    barcode = uiState.value.barcode,
                    purchasePrice = uiState.value.purchasePrice.toDouble(),
                    price = uiState.value.salePrice.toDouble(),
                    stock = uiState.value.stock.toDouble(),
                    photoBytes = uiState.value.photoBytes,
                    category = uiState.value.category,
                    unit = uiState.value.unit
                )

            )
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            errorResult = ""
                        )
                    }
                    _eventChannel.send(InventoryEvents.NewProductSaved)
                    clearForm()
                }
                .onError {
                    _uiState.update {
                        it.copy(
                            errorResult = it.toString()
                        )
                    }
                }
        }
    }
}