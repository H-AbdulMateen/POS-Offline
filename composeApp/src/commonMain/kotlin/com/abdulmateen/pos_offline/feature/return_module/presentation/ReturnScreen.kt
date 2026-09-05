package com.abdulmateen.pos_offline.feature.return_module.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReturnScreenRoot() {
    val viewModel = koinViewModel<ReturnViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ReturnScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onOrderClick = { viewModel.selectOrder(it.orderId) },
        onDismissDetails = { viewModel.selectOrder(null) },
        onProcessReturn = viewModel::processReturn
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnScreen(
    uiState: ReturnUiState,
    onSearchQueryChange: (String) -> Unit,
    onOrderClick: (Order) -> Unit,
    onDismissDetails: () -> Unit,
    onProcessReturn: (String?, List<com.abdulmateen.pos_offline.data.database.entities.OrderItemEntity>) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Return Management") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearchQueryChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search Order by ID, Customer or Phone...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Select Order to Return", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.small
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ReturnHeaderCell("Date", 1.2f)
                        ReturnHeaderCell("Order #", 1f)
                        ReturnHeaderCell("Customer", 2f)
                        ReturnHeaderCell("Total (${uiState.currencySymbol})", 1.2f, textAlign = TextAlign.End, isLast = true)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.orders) { order ->
                            ReturnOrderRow(
                                order = order,
                                currencySymbol = uiState.currencySymbol,
                                onClick = { onOrderClick(order) }
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Recent Returns", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(0.6f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.small
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ReturnHeaderCell("Date", 1.2f)
                        ReturnHeaderCell("Order #", 1f)
                        ReturnHeaderCell("Reason", 2f)
                        ReturnHeaderCell("Refund (${uiState.currencySymbol})", 1.2f, textAlign = TextAlign.End, isLast = true)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.returns) { returnEntry ->
                            RecentReturnRow(returnEntry = returnEntry, currencySymbol = uiState.currencySymbol)
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }
                }
            }
        }
    }

    if (uiState.selectedOrder != null) {
        ReturnItemsDialog(
            orderDetails = uiState.selectedOrder,
            currencySymbol = uiState.currencySymbol,
            onDismiss = onDismissDetails,
            onConfirmReturn = onProcessReturn
        )
    }
}

@Composable
fun ReturnItemsDialog(
    orderDetails: com.abdulmateen.pos_offline.data.database.entities.OrderWithItems,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirmReturn: (String?, List<com.abdulmateen.pos_offline.data.database.entities.OrderItemEntity>) -> Unit
) {
    var reason by remember { mutableStateOf("") }
    val selectedItems = remember { mutableStateListOf<com.abdulmateen.pos_offline.data.database.entities.OrderItemEntity>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirmReturn(reason.takeIf { it.isNotBlank() }, selectedItems.toList())
                    onDismiss()
                },
                enabled = selectedItems.isNotEmpty()
            ) {
                Text("Confirm Return")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Return Order #${orderDetails.order.orderId}") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Select items to return:")
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                    items(orderDetails.items) { item ->
                        val isSelected = selectedItems.contains(item)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    if (isSelected) selectedItems.remove(item) else selectedItems.add(item)
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { 
                                    if (it == true) selectedItems.add(item) else selectedItems.remove(item)
                                }
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = item.productName, style = MaterialTheme.typography.bodySmall)
                                Text(text = "${item.quantity} x $currencySymbol ${item.price}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                            Text(text = "$currencySymbol ${item.price * item.quantity}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason for return (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun RecentReturnRow(returnEntry: com.abdulmateen.pos_offline.data.database.entities.ReturnEntity, currencySymbol: String) {
    val date = remember(returnEntry.createdAt) {
        val localDateTime = kotlin.time.Instant.fromEpochMilliseconds(returnEntry.createdAt)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.day.toString().padStart(2, '0')
        val month = localDateTime.month.number.toString().padStart(2, '0')
        val year = (localDateTime.year % 100).toString().padStart(2, '0')
        "$day-$month-$year"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReturnTableCell(date, 1.2f)
        ReturnTableCell("#${returnEntry.orderId}", 1f)
        ReturnTableCell(returnEntry.reason ?: "-", 2f)
        ReturnTableCell("$currencySymbol ${returnEntry.totalReturnAmount}", 1.2f, textAlign = TextAlign.End, isLast = true)
    }
}

@Composable
fun ReturnOrderRow(order: Order, currencySymbol: String, onClick: () -> Unit) {
    val date = remember(order.createdAt) {
        val localDateTime = kotlin.time.Instant.fromEpochMilliseconds(order.createdAt)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.day.toString().padStart(2, '0')
        val month = localDateTime.month.number.toString().padStart(2, '0')
        val year = (localDateTime.year % 100).toString().padStart(2, '0')
        "$day-$month-$year"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReturnTableCell(date, 1.2f)
        ReturnTableCell("#${order.orderId}", 1f)
        Column(modifier = Modifier.weight(2f).padding(12.dp)) {
            Text(text = order.customerName ?: "Walk-in Customer", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            if (!order.customerPhone.isNullOrBlank()) {
                Text(text = order.customerPhone, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.fillMaxHeight())
        ReturnTableCell("$currencySymbol ${order.total}", 1.2f, textAlign = TextAlign.End, isLast = true)
    }
}

@Composable
fun RowScope.ReturnHeaderCell(
    text: String,
    weight: Float,
    textAlign: TextAlign = TextAlign.Start,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium,
            textAlign = textAlign
        )
        if (!isLast) {
            VerticalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Composable
fun RowScope.ReturnTableCell(
    text: String,
    weight: Float,
    textAlign: TextAlign = TextAlign.Start,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            textAlign = textAlign
        )
        if (!isLast) {
            VerticalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}
