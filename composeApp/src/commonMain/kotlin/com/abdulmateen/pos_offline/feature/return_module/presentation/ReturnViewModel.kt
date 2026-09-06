package com.abdulmateen.pos_offline.feature.return_module.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.entities.ReturnEntity
import com.abdulmateen.pos_offline.data.database.entities.ReturnItemEntity
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import com.abdulmateen.pos_offline.domain.repository.ReturnRepository
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ReturnUiState(
    val orders: List<Order> = emptyList(),
    val selectedOrder: com.abdulmateen.pos_offline.data.database.entities.OrderWithItems? = null,
    val returns: List<ReturnEntity> = emptyList(),
    val currencySymbol: String = "$",
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false
)

class ReturnViewModel(
    private val orderRepository: OrderRepository,
    private val returnRepository: ReturnRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _selectedOrderId = MutableStateFlow<Long?>(null)
    private val _uiState = MutableStateFlow(ReturnUiState())
    val uiState: StateFlow<ReturnUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20

    init {
        loadNextOrders()
        observeDataStore()
        observeSearch()
        loadReturns()
    }

    private fun loadReturns() {
        viewModelScope.launch {
            returnRepository.getAllReturns().collect { returns ->
                _uiState.update { it.copy(returns = returns) }
            }
        }
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            val symbol = dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL)
            _uiState.update { it.copy(currencySymbol = symbol.ifEmpty { "$" }) }
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery.debounce(300).collect { query ->
                if (query.isEmpty()) {
                    currentPage = 0
                    _uiState.update { it.copy(orders = emptyList(), isEndReached = false) }
                    loadNextOrders()
                } else {
                    orderRepository.getAllOrders().collect { orders ->
                        val filtered = orders.filter { 
                            it.customerName?.contains(query, ignoreCase = true) == true ||
                            it.customerPhone?.contains(query) == true ||
                            it.orderId.toString() == query
                        }
                        _uiState.update { 
                            it.copy(
                                orders = filtered,
                                isEndReached = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadNextOrders() {
        if (_uiState.value.isLoading || _uiState.value.isEndReached || _searchQuery.value.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            orderRepository.getOrdersPaged(pageSize, currentPage * pageSize).firstOrNull()?.let { newOrders ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        orders = it.orders + newOrders,
                        isEndReached = newOrders.size < pageSize
                    )
                }
                currentPage++
            } ?: _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectOrder(orderId: Long?) {
        _selectedOrderId.value = orderId
        if (orderId != null) {
            viewModelScope.launch {
                orderRepository.getOrderWithItems(orderId).collect { details ->
                    _uiState.update { it.copy(selectedOrder = details) }
                }
            }
        } else {
            _uiState.update { it.copy(selectedOrder = null) }
        }
    }

    fun processReturn(reason: String?, itemsToReturn: List<com.abdulmateen.pos_offline.data.database.entities.OrderItemEntity>) {
        val selectedOrder = uiState.value.selectedOrder ?: return
        viewModelScope.launch {
            val totalReturnAmount = itemsToReturn.sumOf { it.price * it.quantity }
            val returnEntity = ReturnEntity(
                orderId = selectedOrder.order.orderId,
                totalReturnAmount = totalReturnAmount,
                reason = reason
            )
            val returnItems = itemsToReturn.map { 
                ReturnItemEntity(
                    returnId = 0,
                    productId = it.productId,
                    quantity = it.quantity,
                    amount = it.price * it.quantity
                )
            }
            returnRepository.createReturn(returnEntity, returnItems)
            _selectedOrderId.value = null
        }
    }
}
