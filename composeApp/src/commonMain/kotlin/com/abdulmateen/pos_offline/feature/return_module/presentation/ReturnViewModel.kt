package com.abdulmateen.pos_offline.feature.return_module.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.data.database.entities.ReturnEntity
import com.abdulmateen.pos_offline.data.database.entities.ReturnItemEntity
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.domain.repository.OrderRepository
import com.abdulmateen.pos_offline.domain.repository.ReturnRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ReturnUiState(
    val orders: List<Order> = emptyList(),
    val selectedOrder: com.abdulmateen.pos_offline.data.database.entities.OrderWithItems? = null,
    val returns: List<ReturnEntity> = emptyList(),
    val isLoading: Boolean = false
)

class ReturnViewModel(
    private val orderRepository: OrderRepository,
    private val returnRepository: ReturnRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _selectedOrderId = MutableStateFlow<Long?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ReturnUiState> = combine(
        _searchQuery,
        _selectedOrderId,
        orderRepository.getAllOrders(),
        returnRepository.getAllReturns()
    ) { query, selectedId, orders, returns ->
        val filteredOrders = if (query.isEmpty()) {
            orders
        } else {
            orders.filter { 
                it.customerName?.contains(query, ignoreCase = true) == true ||
                it.customerPhone?.contains(query) == true ||
                it.orderId.toString() == query
            }
        }
        val sortedOrders = filteredOrders.sortedByDescending { it.createdAt }
        
        if (selectedId != null) {
            orderRepository.getOrderWithItems(selectedId).map { details ->
                ReturnUiState(
                    orders = sortedOrders,
                    selectedOrder = details,
                    returns = returns
                )
            }.first()
        } else {
            ReturnUiState(orders = sortedOrders, returns = returns)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReturnUiState())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectOrder(orderId: Long?) {
        _selectedOrderId.value = orderId
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
