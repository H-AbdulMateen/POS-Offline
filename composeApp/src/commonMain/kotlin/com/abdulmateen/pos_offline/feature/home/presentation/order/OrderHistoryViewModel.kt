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
    val isLoading: Boolean = false
)

class OrderHistoryViewModel(
    private val orderRepository: OrderRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _selectedOrderId = MutableStateFlow<Long?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<OrderHistoryUiState> = combine(
        _searchQuery,
        _selectedOrderId,
        orderRepository.getAllOrders()
    ) { query, selectedId, orders ->
        val filteredOrders = if (query.isEmpty()) {
            orders
        } else {
            orders.filter { 
                it.customerName?.contains(query, ignoreCase = true) == true ||
                it.customerPhone?.contains(query) == true
            }
        }
        val sortedOrders = filteredOrders.sortedByDescending { it.createdAt }
        val businessName = dataStoreManager.getStringValue(PrefKeys.BUSINESS_NAME)
        val currencySymbol = dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL).ifEmpty { "$" }

        if (selectedId != null) {
            orderRepository.getOrderWithItems(selectedId).map { details ->
                OrderHistoryUiState(
                    orders = sortedOrders,
                    selectedOrderDetails = details,
                    businessName = businessName,
                    currencySymbol = currencySymbol
                )
            }.first()
        } else {
            OrderHistoryUiState(
                orders = sortedOrders,
                businessName = businessName,
                currencySymbol = currencySymbol
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OrderHistoryUiState())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectOrder(orderId: Long?) {
        _selectedOrderId.value = orderId
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.deleteOrder(order.orderId)
        }
    }
}
