package com.abdulmateen.pos_offline.feature.customer.presentation

import com.abdulmateen.pos_offline.domain.models.Customer
import org.jetbrains.compose.resources.StringResource

data class CustomerUiState(
    val isLoading: Boolean = false,
    val customers: List<Customer> = emptyList(),
    val searchQuery: String = "",
    val isAddEditDialogVisible: Boolean = false,
    val selectedCustomer: Customer? = null,
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val nameError: String? = null,
    val phoneError: String? = null
)

sealed interface CustomerUiAction {
    data class OnSearchQueryChange(val query: String) : CustomerUiAction
    data object OnAddCustomerClick : CustomerUiAction
    data class OnEditCustomerClick(val customer: Customer) : CustomerUiAction
    data class OnDeleteCustomerClick(val customer: Customer) : CustomerUiAction
    data object OnSaveCustomerClick : CustomerUiAction
    data object OnDismissDialog : CustomerUiAction
    
    data class OnNameChange(val name: String) : CustomerUiAction
    data class OnPhoneChange(val phone: String) : CustomerUiAction
    data class OnEmailChange(val email: String) : CustomerUiAction
    data class OnAddressChange(val address: String) : CustomerUiAction
    
    data class OnCustomerClick(val customerId: Long) : CustomerUiAction
}

sealed interface CustomerEvents {
    data class Success(val message: StringResource) : CustomerEvents
    data class Error(val message: StringResource) : CustomerEvents
}
