package com.abdulmateen.pos_offline.feature.customer.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.layouts.LoadingSection
import com.abdulmateen.pos_offline.data.database.entities.OrderWithItems
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.feature.home.presentation.utils.generateInvoiceInPdf
import com.abdulmateen.pos_offline.feature.home.presentation.utils.printPdf
import com.abdulmateen.pos_offline.feature.home.presentation.utils.shareInvoiceFile
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pos_offline.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerLedgerScreenRoot(
    customerId: Long,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<CustomerLedgerViewModel> { parametersOf(customerId) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.customerName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.exportToCsv() }) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export CSV")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            LoadingSection(modifier = Modifier.fillMaxSize().padding(padding))
        } else {
            CustomerLedgerScreen(
                uiState = uiState,
                onTransactionClick = viewModel::onTransactionClick,
                modifier = Modifier.padding(padding)
            )
        }
    }

    if (uiState.showOrderDetails && uiState.selectedOrder != null) {
        OrderDetailDialog(
            orderWithItems = uiState.selectedOrder,
            onDismiss = { viewModel.dismissOrderDetails() },
            businessName = uiState.businessName,
            currencySymbol = uiState.currencySymbol
        )
    }
}

@Composable
fun CustomerLedgerScreen(
    uiState: CustomerLedgerUiState,
    onTransactionClick: (LedgerTransaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.outstanding_balance),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "Rs. ${uiState.totalOutstandingBalance}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = stringResource(Res.string.ledger),
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(Res.string.transaction_date), modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
                    Text(text = stringResource(Res.string.transaction_type), modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
                    Text(text = stringResource(Res.string.amount), modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
                }
            }
            items(uiState.transactions) { transaction ->
                LedgerItem(
                    transaction = transaction,
                    onClick = { onTransactionClick(transaction) }
                )
            }
        }
    }
}

@Composable
fun LedgerItem(
    transaction: LedgerTransaction,
    onClick: () -> Unit
) {
    val date = kotlin.time.Instant.fromEpochMilliseconds(transaction.date)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${date.day}/${date.month.number}/${date.year}",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = transaction.type,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Rs. ${transaction.amount}",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == "Order") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun OrderDetailDialog(
    orderWithItems: OrderWithItems,
    onDismiss: () -> Unit,
    businessName: String,
    currencySymbol: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Order Details #${orderWithItems.order.orderId}") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total:", fontWeight = FontWeight.Bold)
                    Text("Rs. ${orderWithItems.order.total}")
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Payment:", fontWeight = FontWeight.Bold)
                    Text(orderWithItems.order.paymentMethod)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Items:", fontWeight = FontWeight.Bold)
                HorizontalDivider()
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    items(orderWithItems.items) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.productName, style = MaterialTheme.typography.bodyMedium)
                                Text("${item.quantity} x Rs. ${item.price}", style = MaterialTheme.typography.bodySmall)
                            }
                            Text("Rs. ${item.quantity * item.price}", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val fileName = "invoice_${orderWithItems.order.orderId}.pdf"
                val invoiceData = generateInvoiceInPdf(
                    cartItems = orderWithItems.items.map {
                        CartItem(
                            productId = it.productId,
                            productName = it.productName,
                            sku = it.sku,
                            quantity = it.quantity,
                            price = it.price * it.quantity,
                            discount = it.discount,
                            unitPrice = it.price,
                            imagePath = it.imagePath
                        )
                    },
                    subTotal = orderWithItems.order.subTotal,
                    discount = orderWithItems.order.discount ?: 0.0,
                    tax = orderWithItems.order.tax ?: 0.0,
                    total = orderWithItems.order.total,
                    paidAmount = orderWithItems.order.total,
                    change = 0.0,
                    paymentType = orderWithItems.order.paymentMethod,
                    businessName = businessName,
                    currencySymbol = currencySymbol
                )
                printPdf(invoiceData, fileName)
            }) {
                Text("Print")
            }
            Button(onClick = {
                val fileName = "invoice_${orderWithItems.order.orderId}.pdf"
                val invoiceData = generateInvoiceInPdf(
                    cartItems = orderWithItems.items.map {
                        CartItem(
                            productId = it.productId,
                            productName = it.productName,
                            sku = it.sku,
                            quantity = it.quantity,
                            price = it.price * it.quantity,
                            discount = it.discount,
                            unitPrice = it.price,
                            imagePath = it.imagePath
                        )
                    },
                    subTotal = orderWithItems.order.subTotal,
                    discount = orderWithItems.order.discount ?: 0.0,
                    tax = orderWithItems.order.tax ?: 0.0,
                    total = orderWithItems.order.total,
                    paidAmount = orderWithItems.order.total,
                    change = 0.0,
                    paymentType = orderWithItems.order.paymentMethod,
                    businessName = businessName,
                    currencySymbol = currencySymbol
                )
                shareInvoiceFile(invoiceData, fileName)
            }) {
                Text("Share")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
