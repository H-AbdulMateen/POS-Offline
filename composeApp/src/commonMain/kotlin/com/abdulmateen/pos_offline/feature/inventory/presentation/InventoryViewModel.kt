package com.abdulmateen.pos_offline.feature.inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.common.presentation.utils.Patterns
import com.abdulmateen.pos_offline.core.designsystem.toUiText
import com.abdulmateen.pos_offline.core.domain.onError
import com.abdulmateen.pos_offline.core.domain.onSuccess
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.ProductDetail
import com.abdulmateen.pos_offline.domain.use_cases.ProductUseCases
import com.abdulmateen.pos_offline.feature.inventory.presentation.models.toProductUi
import com.abdulmateen.pos_offline.utils.Validator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.cant_delete_this_product
import pos_offline.composeapp.generated.resources.category_added
import pos_offline.composeapp.generated.resources.product_added
import pos_offline.composeapp.generated.resources.unit_added
import kotlin.collections.map
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class InventoryViewModel constructor(
    private val repository: InventoryRepository,
    private val productUseCases: ProductUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.onStart {
        loadCategories()
        loadUnits()
        loadProducts()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState()
    )
    private val _eventChannel = Channel<com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryEvents>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _searchProductQuery = MutableStateFlow("")
    val searchProductQuery: StateFlow<String> = _searchProductQuery.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20

    init {
        viewModelScope.launch {
            _searchProductQuery.debounce(600.milliseconds)
                .distinctUntilChanged()
                .mapLatest { query ->
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    repository.searchProduct(query)
                }
                .collect { filteredList ->
                    filteredList.map { products ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                productList = products.map { product -> product.toProductUi() }
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

    private fun loadUnits() {
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

            is InventoryUiAction.OnAddItemClick -> {
                validateFields(action.isEditing)
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
                uiState.value.selectedProductId?.let {
                    deleteProduct(it)
                }
            }

            is InventoryUiAction.OnEditItemClick -> {
                viewModelScope.launch {
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
                if (inputPurchasePrice.isEmpty() || inputPurchasePrice.matches(Patterns.decimalRegex)) {
                    _uiState.update {
                        it.copy(
                            purchasePrice = inputPurchasePrice,
                            hasPurchasePriceError = false,
                            purchasePriceErrorText = ""
                        )
                    }
                } else {
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

                if (inputQuantity.isEmpty() || inputQuantity.matches(Patterns.decimalRegex)) {
                    _uiState.update {
                        it.copy(
                            stock = inputQuantity,
                            hasStockError = false,
                            stockErrorText = ""
                        )
                    }
                } else {
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
                } else {
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

            InventoryUiAction.ToggleCategoryDialog -> {
                _uiState.update {
                    it.copy(
                        categoryDialogVisible = !it.categoryDialogVisible
                    )
                }
            }

            InventoryUiAction.ToggleUnitDialog -> {
                _uiState.update {
                    it.copy(
                        unitDialogVisible = !it.unitDialogVisible
                    )
                }
            }


            InventoryUiAction.OnAddNewCategory -> {
                addNewCategory()
            }

            InventoryUiAction.OnAddNewUnit -> {
                addNewUnit()
            }

            is InventoryUiAction.ToggleOptionReveal -> {
                _uiState.update {
                    it.copy(
                        productList = uiState.value.productList.map { product ->
                            if (product.productId == action.productId) {
                                product.copy(isOptionRevealed = action.isRevealed)
                            } else {
                                product.copy(isOptionRevealed = false) // auto-close others
                            }
                        }
                    )
                }
            }

            is InventoryUiAction.ToggleDeleteDialog -> {
                _uiState.update {
                    it.copy(
                        showDeleteDialog = !it.showDeleteDialog,
                        selectedProductId = action.productId
                    )
                }
            }

            is InventoryUiAction.ToggleDetailDialog -> {

                if (action.productId != null) {

                    viewModelScope.launch {
                        val product = productUseCases.getProductDetail(action.productId).firstOrNull()
                        _uiState.update {
                            it.copy(
                                detailProductDialog = !it.detailProductDialog,
                                selectedProduct = product
                            )
                        }
                    }
                }else{
                    _uiState.update {
                        it.copy(
                            detailProductDialog = !it.detailProductDialog,
                            selectedProduct = null
                        )
                    }
                }
            }

            is InventoryUiAction.ToggleAddEditProductDialog -> {
                if (action.productId != null) {
                    viewModelScope.launch {
                        val product = productUseCases.getProductDetail(action.productId).firstOrNull()
                        _uiState.update {
                            it.copy(
                                addEditProductDialog = !it.addEditProductDialog,
                                selectedProduct = product
                            )
                        }
                        setUPFieldValues()
                    }
                }else{
                    _uiState.update {
                        it.copy(
                            addEditProductDialog = !it.addEditProductDialog,
                            selectedProduct = null
                        )
                    }
                }
            }
        }
    }

    private fun setUPFieldValues() {
        uiState.value.selectedProduct?.let {product ->
            _uiState.update {
                it.copy(
                    name = product.name,
                    sku = product.sku,
                    barcode = product.barcode,
                    purchasePrice = product.purchasePrice.toString(),
                    salePrice = product.price.toString(),
                    stock = product.stock.toString(),
                    category = product.category,
                    unit = product.unit,
                    photoBytes = product.photoBytes
                )
            }
        }
    }

    private fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            val isDeleted = productUseCases.deleteProduct(productId)
            _uiState.update {
                it.copy(
                    selectedProduct = null,
                    showDeleteDialog = false
                )
            }
            if (!isDeleted) {
                _eventChannel.send(InventoryEvents.OnError(Res.string.cant_delete_this_product))
            }
        }
    }

    private fun addNewUnit() {
        viewModelScope.launch {
            val validateUnitName = Validator.validateNonEmpty(uiState.value.itemUnitName)
            if (!validateUnitName.isValid) {
                _uiState.update {
                    it.copy(
                        hasItemUnitNameError = true,
                        itemUnitNameErrorText = validateUnitName.errorMessage
                    )
                }
                return@launch
            }

            val validateUnitSymbol = Validator.validateNonEmpty(uiState.value.itemUnitSymbol)
            if (!validateUnitSymbol.isValid) {
                _uiState.update {
                    it.copy(
                        hasItemUnitSymbolError = true,
                        itemUnitSymbolErrorText = validateUnitSymbol.errorMessage
                    )
                }
                return@launch
            }

            repository.insertUnit(
                unit = ItemUnit(
                    name = uiState.value.itemUnitName,
                    symbol = uiState.value.itemUnitSymbol
                )
            ).onSuccess {
                _eventChannel.send(InventoryEvents.OnSuccess(Res.string.unit_added))
                _uiState.update {
                    it.copy(
                        itemUnitName = "",
                        itemUnitSymbol = "",
                        unitErrorResult = null,
                        unitDialogVisible = false
                    )
                }
            }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            unitErrorResult = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun addNewCategory() {
        viewModelScope.launch {
            val validateCategoryName = Validator.validateNonEmpty(uiState.value.categoryName)
            if (!validateCategoryName.isValid) {
                _uiState.update {
                    it.copy(
                        hasCategoryNameError = true,
                        categoryNameErrorText = validateCategoryName.errorMessage
                    )
                }
                return@launch
            }
            repository.insertCategory(
                Category(
                    name = uiState.value.categoryName
                )
            ).onSuccess {
                _uiState.update {
                    _eventChannel.send(_root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryEvents.OnSuccess(Res.string.category_added))
                    it.copy(
                        categoryName = "",
                        categoryErrorResult = null,
                        categoryDialogVisible = false
                    )
                }
            }.onError { error ->
                _uiState.update {
                    it.copy(
                        categoryErrorResult = error.toUiText()
                    )
                }
            }
        }
    }

    private fun loadProducts() {
        currentPage = 0
        _uiState.update { it.copy(productList = emptyList(), isEndReached = false) }
        loadNextProducts()
    }

    fun loadNextProducts() {
        if (_uiState.value.isLoading || _uiState.value.isEndReached || _searchProductQuery.value.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getProductsPaged(pageSize, currentPage * pageSize).firstOrNull()?.let { products ->
                val uiProducts = products.map { it.toProductUi() }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        productList = it.productList + uiProducts,
                        isEndReached = products.size < pageSize
                    )
                }
                currentPage++
            } ?: _uiState.update { it.copy(isLoading = false) }
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

    private fun validateFields(isEditing: Boolean) {
        viewModelScope.launch {
            val validateName = Validator.validateNonEmpty(uiState.value.name)
            if (!validateName.isValid) {
                _uiState.update {
                    it.copy(
                        hasNameError = true,
                        nameErrorText = validateName.errorMessage
                    )
                }
                return@launch
            }
            val validateSku = Validator.validateNonEmpty(uiState.value.sku)
            if (!validateSku.isValid) {
                _uiState.update {
                    it.copy(
                        hasSkuError = true,
                        skuErrorText = validateSku.errorMessage
                    )
                }
                return@launch
            }
            val validateBarcode = Validator.validateNonEmpty(uiState.value.barcode)
            if (!validateBarcode.isValid) {
                _uiState.update {
                    it.copy(
                        hasBarcodeError = true,
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
                        hasPurchasePriceError = true,
                        purchasePriceErrorText = validatePurchasePrice.errorMessage
                    )
                }
                return@launch
            }

            val validateSalesPrice = Validator.validateNonEmpty(uiState.value.salePrice)
            if (!validateSalesPrice.isValid) {
                _uiState.update {
                    it.copy(
                        hasSalesPriceError = true,
                        salesPriceErrorText = validateSalesPrice.errorMessage
                    )
                }
                return@launch
            }

            val validateQuantity = Validator.validateNonEmpty(uiState.value.stock)
            if (!validateQuantity.isValid) {
                _uiState.update {
                    it.copy(
                        hasStockError = true,
                        stockErrorText = validateQuantity.errorMessage
                    )
                }
                return@launch
            }

            val validateCategory = Validator.validateNonEmpty(uiState.value.category?.name!!)
            if (!validateCategory.isValid) {
                _uiState.update {
                    it.copy(
                        hasCategoryError = true,
                        categoryErrorText = validateCategory.errorMessage
                    )
                }
                return@launch
            }

            val validateUnit = Validator.validateNonEmpty(uiState.value.unit?.name!!)
            if (!validateUnit.isValid) {
                _uiState.update {
                    it.copy(
                        hasUnitError = true,
                        unitErrorText = validateUnit.errorMessage
                    )
                }
                return@launch
            }
            val productDetail = ProductDetail(
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
            if (!isEditing) {
                insertProduct(productDetail)
            } else {
                val product = uiState.value.selectedProduct!!.copy(
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
                updateProduct(product)
            }
        }
    }

    private suspend fun insertProduct(productDetail: ProductDetail) {
        repository.insertProduct(productDetail)
            .onSuccess {
                _uiState.update {
                    it.copy(
                        errorResult = null,
                        addEditProductDialog = false
                    )
                }
                _eventChannel.send(_root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryEvents.OnSuccess(Res.string.product_added))
                clearForm()
            }
            .onError { error ->
                _uiState.update {
                    it.copy(
                        errorResult = error.toUiText()
                    )
                }
            }
    }

    private suspend fun updateProduct(productDetail: ProductDetail) {
        repository.updateProduct(productDetail)
        _uiState.update {
            it.copy(
                addEditProductDialog = false,
                selectedProduct = null,
            )
        }
    }

    private fun getProductDetail(productId: Long) {
        viewModelScope.launch {
            val product = productUseCases.getProductDetail(productId).firstOrNull()
            product?.let {
                _uiState.update {
                    it.copy(
                        selectedProduct = product
                    )
                }
            }
        }
    }
}