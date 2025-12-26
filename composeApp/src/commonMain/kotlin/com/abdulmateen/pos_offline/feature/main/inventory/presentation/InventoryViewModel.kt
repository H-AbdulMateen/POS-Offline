package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.feature.main.home.domain.InventoryRepository
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ProductDetail
import com.abdulmateen.pos_offline.utils.Validator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    private val _newProduct = MutableStateFlow(ProductDetail.empty())
    var newProduct: StateFlow<ProductDetail> = _newProduct

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


    fun uiAction(action: InventoryUiAction) {
        when (action) {
            InventoryUiAction.ClearForm -> {
                clearForm()
            }

            InventoryUiAction.OnAddItemClick -> {
                viewModelScope.launch {
                    validateFields()
                }
            }

            is InventoryUiAction.OnBarcodeChange -> {
                _newProduct.update {
                    it.copy(
                        barcode = action.barcode
                    )
                }

            }

            is InventoryUiAction.OnCategoryChange -> {
                _newProduct.update {
                    it.copy(
                        category = action.category
                    )
                }
            }

            is InventoryUiAction.OnDeleteItemClick -> {
                _uiState.update {
                    it.copy(
                        productList = it.productList.filter { product -> product != action.product }
                    )
                }
            }

            is InventoryUiAction.OnDescriptionChange -> {
                _newProduct.update {
                    it.copy(
                        description = action.description
                    )
                }
            }

            is InventoryUiAction.OnEditItemClick -> {
                viewModelScope.launch {
                    repository.getProductById(action.productId)?.let {
                        _newProduct.update { it }
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
                        imageBitmap = action.imageBitmap
                    )
                }
                _newProduct.update {
                    it.copy(
                        photoBytes = action.bytes
                    )
                }
            }

            is InventoryUiAction.OnNameChange -> {
                _newProduct.update {
                    it.copy(
                        name = action.name
                    )
                }
            }

            is InventoryUiAction.OnPurchasePriceChange -> {
                _newProduct.update {
                    it.copy(
                        purchasePrice = action.purchasePrice.toDouble()
                    )
                }
            }

            is InventoryUiAction.OnQuantityChange -> {
                _newProduct.update {
                    it.copy(
                        quantity = action.quantity.toDouble()
                    )
                }
            }

            is InventoryUiAction.OnSalesPriceChange -> {
                _newProduct.update {
                    it.copy(
                        price = action.salesPrice.toDouble()
                    )
                }
            }

            is InventoryUiAction.OnSearchProductChange -> {
                _uiState.update {
                    it.copy(
                        searchProduct = action.searchProduct
                    )
                }
            }

            is InventoryUiAction.OnSkuChange -> {
                _newProduct.update {
                    it.copy(
                        sku = action.sku
                    )
                }
            }

            is InventoryUiAction.OnItemUnitChange -> {
                _newProduct.update {
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
        _newProduct.value = ProductDetail.empty()
    }

    private suspend fun validateFields() {
        val validateName = Validator.validateNonEmpty(newProduct.value.name)
        if (!validateName.isValid) {
            _uiState.update {
                it.copy(
                    hasNameError = validateName.isValid,
                    nameErrorText = validateName.errorMessage
                )
            }
            return
        }
        val validateSku = Validator.validateNonEmpty(newProduct.value.sku)
        if (!validateSku.isValid) {
            _uiState.update {
                it.copy(
                    hasSkuError = validateSku.isValid,
                    skuErrorText = validateSku.errorMessage
                )
            }
            return
        }
        val validateBarcode = Validator.validateNonEmpty(newProduct.value.barcode)
        if (!validateBarcode.isValid) {
            _uiState.update {
                it.copy(
                    hasBarcodeError = validateBarcode.isValid,
                    barcodeErrorText = validateBarcode.errorMessage
                )
            }
            return
        }
        val validatePurchasePrice =
            Validator.validateNonEmpty(newProduct.value.purchasePrice.toString())
        if (!validatePurchasePrice.isValid) {
            _uiState.update {
                it.copy(
                    hasPurchasePriceError = validatePurchasePrice.isValid,
                    purchasePriceErrorText = validatePurchasePrice.errorMessage
                )
            }
            return
        }

        val validateSalesPrice = Validator.validateNonEmpty(newProduct.value.price.toString())
        if (!validateSalesPrice.isValid) {
            _uiState.update {
                it.copy(
                    hasSalesPriceError = validateSalesPrice.isValid,
                    salesPriceErrorText = validateSalesPrice.errorMessage
                )
            }
            return
        }

        val validateQuantity = Validator.validateNonEmpty(newProduct.value.quantity.toString())
        if (!validateQuantity.isValid) {
            _uiState.update {
                it.copy(
                    hasQuantityError = validateQuantity.isValid,
                    quantityErrorText = validateQuantity.errorMessage
                )
            }
            return
        }

        val validateCategory = Validator.validateNonEmpty(newProduct.value.category?.name!!)
        if (!validateCategory.isValid) {
            _uiState.update {
                it.copy(
                    hasCategoryError = validateCategory.isValid,
                    categoryErrorText = validateCategory.errorMessage
                )
            }
            return
        }

        val validateUnit = Validator.validateNonEmpty(newProduct.value.unit?.name!!)
        if (!validateUnit.isValid) {
            _uiState.update {
                it.copy(
                    hasUnitError = validateUnit.isValid,
                    unitErrorText = validateUnit.errorMessage
                )
            }
            return
        }

        repository.insertProduct(newProduct.value)
    }
}