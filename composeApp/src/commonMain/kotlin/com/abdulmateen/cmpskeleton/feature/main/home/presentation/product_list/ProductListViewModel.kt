package com.abdulmateen.cmpskeleton.feature.main.home.presentation.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.cmpskeleton.core.domain.DataError
import com.abdulmateen.cmpskeleton.core.domain.onError
import com.abdulmateen.cmpskeleton.core.domain.onSuccess
import com.abdulmateen.cmpskeleton.core.presentation.toUiText
import com.abdulmateen.cmpskeleton.feature.main.home.domain.Product
import com.abdulmateen.cmpskeleton.feature.main.home.domain.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val repository: ProductRepository
): ViewModel() {
    private var observeProductsJob: Job? = null

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState = _uiState
        .onStart {
            loadProducts()
            observeProductsFromCache()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000L),
            ProductListUiState()
        )

    fun uiAction(action: ProductListUiAction){
        when(action){
            is ProductListUiAction.MarkAsFavourite -> {
                markAsFavourite(productId = action.productId)
            }
            is ProductListUiAction.RemoveFromFavourite -> {
                removeFromFavourite(id = action.id)
            }
            is ProductListUiAction.ForceReload -> {
                refreshProducts()
            }
        }
    }

    private fun removeFromFavourite(id: Int) {
        viewModelScope.launch {
            repository.removeFromFavourite(id = id)
        }
    }

    private fun markAsFavourite(productId: Int) {
        viewModelScope.launch {
            repository.markAsFavourite(productId = productId)
        }
    }

    private fun observeProductsFromCache() {
        observeProductsJob?.cancel()
        observeProductsJob = repository.loadAllProductsFromCache()
            .onEach { products ->
                _uiState.update {
                    it.copy(
                        productList = products
                    )
                }
            }.launchIn(viewModelScope)
    }


    private fun loadProducts() {
        viewModelScope.launch {
                updateLoadingState()
            repository
                .fetchProducts()
                .onSuccess { list ->
                    updateUiStateWithSuccess(list = list)
                }
                .onError { error ->
                    updateUiStateWithError(error = error)
                    observeProductsFromCache()
                }
        }
    }
    private fun refreshProducts() {
        viewModelScope.launch {
                updateRefreshingState()
            repository
                .fetchProducts()
                .onSuccess { list ->
                    updateUiStateWithSuccess(list)
                }
                .onError { error ->
                    updateUiStateWithError(error)
                    observeProductsFromCache()
                }
        }
    }

    private fun updateLoadingState(){
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
    }

    private fun updateRefreshingState(){
        _uiState.update {
            it.copy(
                isRefreshing = true
            )
        }
    }

    private fun updateUiStateWithSuccess(
        list: List<Product>
    ){
        _uiState.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                productList = list,
                errorMessage = null
            )
        }
    }

    private fun updateUiStateWithError(
        error: DataError.Remote
    ){
        _uiState.update {
            it.copy(
                productList = emptyList(),
                isLoading = false,
                isRefreshing = false,
                errorMessage = error.toUiText()
            )
        }
    }
}