package com.abdulmateen.pos_offline.feature.customer.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.SearchField
import com.abdulmateen.pos_offline.core.designsystem.components.layouts.MySnackBarScaffold
import com.abdulmateen.pos_offline.core.presentation.util.ObserveAsEvents
import com.abdulmateen.pos_offline.domain.models.Customer
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pos_offline.composeapp.generated.resources.*

@Composable
fun CustomerScreenRoot(
    onCustomerClick: (Long) -> Unit
) {
    val viewModel = koinViewModel<CustomerViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.eventChannel) { event ->
        when (event) {
            is CustomerEvents.Success -> snackbarHostState.showSnackbar(getString(event.message))
            is CustomerEvents.Error -> snackbarHostState.showSnackbar(getString(event.message))
        }
    }

    CustomerScreen(
        uiState = uiState,
        onAction = { action ->
            if (action is CustomerUiAction.OnCustomerClick) {
                onCustomerClick(action.customerId)
            } else {
                viewModel.onAction(action)
            }
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun CustomerScreen(
    uiState: CustomerUiState,
    onAction: (CustomerUiAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    MySnackBarScaffold(
        snackbarHostState = snackbarHostState,
        isFloatingActionButtonDocked = true,
        onFabClick = { onAction(CustomerUiAction.OnAddCustomerClick) }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchField(
                value = uiState.searchQuery,
                onValueChange = { onAction(CustomerUiAction.OnSearchQueryChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(Res.string.search)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.customers, key = { it.customerId }) { customer ->
                    CustomerItem(
                        customer = customer,
                        onClick = { onAction(CustomerUiAction.OnCustomerClick(customer.customerId)) },
                        onEditClick = { onAction(CustomerUiAction.OnEditCustomerClick(customer)) },
                        onDeleteClick = { onAction(CustomerUiAction.OnDeleteCustomerClick(customer)) }
                    )
                }
            }
        }

        if (uiState.isAddEditDialogVisible) {
            AddEditCustomerDialog(
                uiState = uiState,
                onAction = onAction
            )
        }
    }
}

@Composable
fun CustomerItem(
    customer: Customer,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = customer.name, style = MaterialTheme.typography.titleMedium)
                customer.phone?.let {
                    Text(text = it, style = MaterialTheme.typography.bodySmall)
                }
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun AddEditCustomerDialog(
    uiState: CustomerUiState,
    onAction: (CustomerUiAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onAction(CustomerUiAction.OnDismissDialog) },
        title = {
            Text(
                text = if (uiState.selectedCustomer == null) 
                    stringResource(Res.string.add_customer) 
                else 
                    stringResource(Res.string.edit_customer)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { onAction(CustomerUiAction.OnNameChange(it)) },
                    label = { Text(stringResource(Res.string.customer_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.nameError != null,
                    supportingText = uiState.nameError?.let { { Text(it) } }
                )
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { onAction(CustomerUiAction.OnPhoneChange(it)) },
                    label = { Text(stringResource(Res.string.phone)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { onAction(CustomerUiAction.OnEmailChange(it)) },
                    label = { Text(stringResource(Res.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.address,
                    onValueChange = { onAction(CustomerUiAction.OnAddressChange(it)) },
                    label = { Text(stringResource(Res.string.address)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onAction(CustomerUiAction.OnSaveCustomerClick) }) {
                Text(stringResource(Res.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(CustomerUiAction.OnDismissDialog) }) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}
