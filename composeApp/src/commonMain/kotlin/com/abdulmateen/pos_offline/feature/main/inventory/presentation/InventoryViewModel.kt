package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.feature.main.home.domain.InventoryRepository
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
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
): ViewModel() {
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.onStart {
        loadProducts()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InventoryUiState()
    )

    var newProduct: Product? by mutableStateOf(null)
        private set

    fun uiAction(action: InventoryUiAction){
        when(action){
            InventoryUiAction.ClearForm -> {
                clearForm()
            }
            InventoryUiAction.OnAddItemClick -> {
                validateFields()
            }
            is InventoryUiAction.OnBarcodeChange -> {
                    newProduct = newProduct?.copy(
                        barcode = action.barcode
                    )

            }
            is InventoryUiAction.OnCategoryChange -> {
                newProduct = newProduct?.copy(
                    category = action.category
                )
            }
            is InventoryUiAction.OnDeleteItemClick -> {
                _uiState.update {
                    it.copy(
                        productList = it.productList.filter { product -> product != action.product }
                    )
                }
            }
            is InventoryUiAction.OnDescriptionChange -> {
                newProduct = newProduct?.copy(
                    description = action.description
                )
            }
            is InventoryUiAction.OnEditItemClick -> {
            }
            is InventoryUiAction.OnExpiryDateChange -> {
                _uiState.update {
                    it.copy(
                        itemExpiryDate = action.expiryDate
                    )
                }
            }
            is InventoryUiAction.OnImageUrlChange -> {
                newProduct = newProduct?.copy(
                    photoBytes = action.bytes
                )
            }
            is InventoryUiAction.OnNameChange -> {
                _uiState.update {
                    it.copy(
                        name = action.name
                    )
                }
            }
            is InventoryUiAction.OnPurchasePriceChange -> {
                _uiState.update {
                    it.copy(
                        purchasePrice = action.purchasePrice
                    )
                }
            }
            is InventoryUiAction.OnQuantityChange -> {
                _uiState.update {
                    it.copy(
                        quantity = action.quantity
                    )
                }
            }
            is InventoryUiAction.OnSalesPriceChange -> {
                _uiState.update {
                    it.copy(
                        salesPrice = action.salesPrice
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
                _uiState.update {
                    it.copy(
                        sku = action.sku
                    )
                }
            }
        }
    }

    private fun loadProducts(){
        viewModelScope.launch {
            repository.getAllProducts().onEach {products ->
                _uiState.update {
                    it.copy(
                        productList = products
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun clearForm() {

    }

    private fun validateFields() {

    }

}