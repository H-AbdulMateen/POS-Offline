package com.abdulmateen.pos_offline.feature.customer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.domain.models.Customer
import com.abdulmateen.pos_offline.domain.repository.CustomerRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.customer_added
import pos_offline.composeapp.generated.resources.customer_deleted
import pos_offline.composeapp.generated.resources.customer_updated
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
class CustomerViewModel(
    private val repository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<CustomerEvents>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        observeCustomers()
        observeSearch()
    }

    private fun observeCustomers() {
        repository.getAllCustomers()
            .onEach { customers ->
                _uiState.update { it.copy(customers = customers) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeSearch() {
        _searchQuery
            .debounce(500.milliseconds)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    observeCustomers()
                } else {
                    repository.searchCustomers(query)
                        .onEach { filtered ->
                            _uiState.update { it.copy(customers = filtered) }
                        }
                        .launchIn(viewModelScope)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CustomerUiAction) {
        when (action) {
            is CustomerUiAction.OnSearchQueryChange -> {
                _searchQuery.value = action.query
                _uiState.update { it.copy(searchQuery = action.query) }
            }
            CustomerUiAction.OnAddCustomerClick -> {
                _uiState.update {
                    it.copy(
                        isAddEditDialogVisible = true,
                        selectedCustomer = null,
                        name = "",
                        phone = "",
                        email = "",
                        address = "",
                        nameError = null,
                        phoneError = null
                    )
                }
            }
            is CustomerUiAction.OnEditCustomerClick -> {
                _uiState.update {
                    it.copy(
                        isAddEditDialogVisible = true,
                        selectedCustomer = action.customer,
                        name = action.customer.name,
                        phone = action.customer.phone ?: "",
                        email = action.customer.email ?: "",
                        address = action.customer.address ?: "",
                        nameError = null,
                        phoneError = null
                    )
                }
            }
            is CustomerUiAction.OnDeleteCustomerClick -> {
                viewModelScope.launch {
                    repository.deleteCustomer(action.customer)
                    _eventChannel.send(CustomerEvents.Success(Res.string.customer_deleted))
                }
            }
            CustomerUiAction.OnSaveCustomerClick -> {
                saveCustomer()
            }
            CustomerUiAction.OnDismissDialog -> {
                _uiState.update { it.copy(isAddEditDialogVisible = false) }
            }
            is CustomerUiAction.OnNameChange -> {
                _uiState.update { it.copy(name = action.name, nameError = null) }
            }
            is CustomerUiAction.OnPhoneChange -> {
                _uiState.update { it.copy(phone = action.phone, phoneError = null) }
            }
            is CustomerUiAction.OnEmailChange -> {
                _uiState.update { it.copy(email = action.email) }
            }
            is CustomerUiAction.OnAddressChange -> {
                _uiState.update { it.copy(address = action.address) }
            }
            is CustomerUiAction.OnCustomerClick -> {
                // Handled in UI navigation
            }
        }
    }

    private fun saveCustomer() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Name is required") }
            return
        }

        viewModelScope.launch {
            val customer = Customer(
                customerId = state.selectedCustomer?.customerId ?: 0,
                name = state.name,
                phone = state.phone.takeIf { it.isNotBlank() },
                email = state.email.takeIf { it.isNotBlank() },
                address = state.address.takeIf { it.isNotBlank() },
                createdAt = state.selectedCustomer?.createdAt ?: 0
            )
            repository.upsertCustomer(customer)
            _uiState.update { it.copy(isAddEditDialogVisible = false) }
            val message = if (state.selectedCustomer == null) Res.string.customer_added else Res.string.customer_updated
            _eventChannel.send(CustomerEvents.Success(message))
        }
    }
}
