package com.abdulmateen.pos_offline.feature.home.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class OrderHistoryUiState(
    val orders: List<Order> = emptyList(),
    val selectedOrderDetails: com.abdulmateen.pos_offline.data.database.entities.OrderWithItems? = null,
    val businessName: String = "",
    val currencySymbol: String = "$",
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val page: Int = 0
)

class OrderHistoryViewModel(
    private val orderRepository: OrderRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _selectedOrderId = MutableStateFlow<Long?>(null)
    private val _uiState = MutableStateFlow(OrderHistoryUiState())
    
    private var currentPage = 0
    private val pageSize = 20

    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    init {
        loadNextOrders()
        observeDataStore()
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            val businessName = dataStoreManager.getStringValue(PrefKeys.BUSINESS_NAME)
            val currencySymbol = dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL).ifEmpty { "$" }
            _uiState.update { it.copy(businessName = businessName, currencySymbol = currencySymbol) }
        }
    }

    fun loadNextOrders() {
        if (_uiState.value.isLoading || _uiState.value.isEndReached) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            orderRepository.getOrdersPaged(pageSize, currentPage * pageSize)
                .first()
                .let { newOrders ->
                    _uiState.update { 
                        it.copy(
                            orders = it.orders + newOrders,
                            isLoading = false,
                            isEndReached = newOrders.size < pageSize,
                            page = currentPage
                        )
                    }
                    currentPage++
                }
        }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    private val searchFlow = _searchQuery.debounce(300).flatMapLatest { query ->
        if (query.isEmpty()) {
            // Reset to paginated view when query is cleared
            currentPage = 0
            _uiState.update { it.copy(orders = emptyList(), isEndReached = false) }
            loadNextOrders()
            emptyFlow()
        } else {
            orderRepository.getAllOrders().map { orders ->
                orders.filter { 
                    it.customerName?.contains(query, ignoreCase = true) == true ||
                    it.customerPhone?.contains(query) == true
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            searchFlow.collect { filteredOrders ->
                if (_searchQuery.value.isNotEmpty()) {
                    _uiState.update { 
                        it.copy(
                            orders = filteredOrders,
                            isEndReached = true // Disable pagination during search
                        )
                    }
                }
            }
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
                    _uiState.update { it.copy(selectedOrderDetails = details) }
                }
            }
        } else {
            _uiState.update { it.copy(selectedOrderDetails = null) }
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.deleteOrder(order.orderId)
        }
    }
}
