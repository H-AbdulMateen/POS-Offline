package com.abdulmateen.pos_offline.feature.home.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class OrderHistoryUiState(
    val orders: List<Order> = emptyList(),
    val selectedOrderDetails: com.abdulmateen.pos_offline.data.database.entities.OrderWithItems? = null,
    val isLoading: Boolean = false
)

class OrderHistoryViewModel(
    private val orderRepository: OrderRepository
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
        Triple(filteredOrders.sortedByDescending { it.createdAt }, selectedId, query)
    }.flatMapLatest { (filteredOrders, selectedId, _) ->
        if (selectedId != null) {
            orderRepository.getOrderWithItems(selectedId).map { details ->
                OrderHistoryUiState(
                    orders = filteredOrders,
                    selectedOrderDetails = details
                )
            }
        } else {
            flowOf(OrderHistoryUiState(orders = filteredOrders))
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
