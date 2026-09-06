package com.abdulmateen.pos_offline.feature.home.presentation.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.domain.models.Order
import com.abdulmateen.pos_offline.feature.home.presentation.utils.generateInvoiceInPdf
import com.abdulmateen.pos_offline.feature.home.presentation.utils.printPdf
import com.abdulmateen.pos_offline.feature.home.presentation.utils.shareInvoiceFile
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreenRoot(
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<OrderHistoryViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    OrderHistoryScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onOrderClick = { viewModel.selectOrder(it.orderId) },
        onDismissDetails = { viewModel.selectOrder(null) },
        onLoadNextPage = viewModel::loadNextOrders,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    uiState: OrderHistoryUiState,
    onSearchQueryChange: (String) -> Unit,
    onOrderClick: (Order) -> Unit,
    onDismissDetails: () -> Unit,
    onLoadNextPage: () -> Unit,
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order History") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
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
                placeholder = { Text("Search by customer or phone...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.small
            ) {
                Column {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OrderHeaderCell("Date", 1.2f)
                        OrderHeaderCell("Customer Details", 2f)
                        OrderHeaderCell("Total (${uiState.currencySymbol})", 1.2f, textAlign = TextAlign.End, isLast = true)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    val listState = rememberLazyListState()
                    
                    val shouldLoadNext = remember {
                        derivedStateOf {
                            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                                ?: return@derivedStateOf false
                            
                            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 5
                        }
                    }
                    
                    LaunchedEffect(shouldLoadNext.value) {
                        if (shouldLoadNext.value && !uiState.isLoading && !uiState.isEndReached) {
                            onLoadNextPage()
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = listState
                    ) {
                        items(uiState.orders) { order ->
                            OrderHistoryRow(
                                order = order,
                                currencySymbol = uiState.currencySymbol,
                                onClick = { onOrderClick(order) }
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        }

                        item {
                            if (uiState.isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                    
                    LaunchedEffect(uiState.orders.size) {
                        // Very simple scroll-to-end detection could be done with listState
                    }
                }
            }
        }
    }

    if (uiState.selectedOrderDetails != null) {
        OrderDetailDialog(
            orderDetails = uiState.selectedOrderDetails,
            businessName = uiState.businessName,
            currencySymbol = uiState.currencySymbol,
            onDismiss = onDismissDetails
        )
    }
}

@Composable
fun OrderDetailDialog(
    orderDetails: com.abdulmateen.pos_offline.data.database.entities.OrderWithItems,
    businessName: String,
    currencySymbol: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val fileName = "invoice_${orderDetails.order.orderId}.pdf"
                val invoiceData = com.abdulmateen.pos_offline.feature.home.presentation.utils.generateInvoiceInPdf(
                    cartItems = orderDetails.items.map { 
                        com.abdulmateen.pos_offline.domain.models.CartItem(
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
                    subTotal = orderDetails.order.subTotal,
                    discount = orderDetails.order.discount ?: 0.0,
                    tax = orderDetails.order.tax ?: 0.0,
                    total = orderDetails.order.total,
                    paidAmount = orderDetails.order.total,
                    change = 0.0,
                    paymentType = orderDetails.order.paymentMethod,
                    businessName = businessName,
                    currencySymbol = currencySymbol
                )
                printPdf(invoiceData, fileName)
            }) {
                Text("Print")
            }
            Button(onClick = {
                val fileName = "invoice_${orderDetails.order.orderId}.pdf"
                val invoiceData = generateInvoiceInPdf(
                    cartItems = orderDetails.items.map { 
                        com.abdulmateen.pos_offline.domain.models.CartItem(
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
                    subTotal = orderDetails.order.subTotal,
                    discount = orderDetails.order.discount ?: 0.0,
                    tax = orderDetails.order.tax ?: 0.0,
                    total = orderDetails.order.total,
                    paidAmount = orderDetails.order.total,
                    change = 0.0,
                    paymentType = orderDetails.order.paymentMethod,
                    businessName = businessName,
                    currencySymbol = currencySymbol
                )
                shareInvoiceFile(invoiceData, fileName)
            }) {
                Text("Share")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        title = { 
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("Order Receipt", style = MaterialTheme.typography.titleLarge)
                Text("#${orderDetails.order.orderId}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        },
        text = {
            val date = remember(orderDetails.order.createdAt) {
                val localDateTime = kotlin.time.Instant.fromEpochMilliseconds(orderDetails.order.createdAt)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                val day = localDateTime.day.toString().padStart(2, '0')
                val month = localDateTime.month.number.toString().padStart(2, '0')
                val year = (localDateTime.year % 100).toString().padStart(2, '0')
                "$day-$month-$year"
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Date", date)
                DetailRow("Customer", orderDetails.order.customerName ?: "Walk-in")
                if (!orderDetails.order.customerPhone.isNullOrBlank()) {
                    DetailRow("Phone", orderDetails.order.customerPhone)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(12.dp))
                
                Column {
                    orderDetails.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productName, 
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${item.quantity} x $currencySymbol ${item.price}", 
                                    style = MaterialTheme.typography.labelMedium, 
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "$currencySymbol ${item.price * item.quantity}", 
                                style = MaterialTheme.typography.bodyMedium, 
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(12.dp))
                
                DetailRow("Subtotal", "$currencySymbol ${orderDetails.order.subTotal}")
                if (orderDetails.order.discount != null && orderDetails.order.discount > 0.0) {
                    DetailRow("Discount", "- $currencySymbol ${orderDetails.order.discount}", valueColor = MaterialTheme.colorScheme.error)
                }
                if (orderDetails.order.tax != null && orderDetails.order.tax > 0.0) {
                    DetailRow("Tax", "+ $currencySymbol ${orderDetails.order.tax}")
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                DetailRow(
                    label = "Total Amount", 
                    value = "$currencySymbol ${orderDetails.order.total}", 
                    isBold = true,
                    labelStyle = MaterialTheme.typography.titleMedium,
                    valueStyle = MaterialTheme.typography.titleMedium,
                    valueColor = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Payment Method", orderDetails.order.paymentMethod, valueStyle = MaterialTheme.typography.labelLarge)
            }
        }
    )
}

@Composable
fun DetailRow(
    label: String, 
    value: String, 
    isBold: Boolean = false,
    labelStyle: TextStyle = MaterialTheme.typography.bodySmall,
    valueStyle: TextStyle = MaterialTheme.typography.bodySmall,
    valueColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = labelStyle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value, 
            style = valueStyle, 
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = valueColor
        )
    }
}

@Composable
fun RowScope.OrderHeaderCell(
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
fun OrderHistoryRow(order: Order, currencySymbol: String, onClick: () -> Unit) {
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
        OrderTableCell(date, 1.2f)
        Column(modifier = Modifier.weight(2f).padding(12.dp)) {
            Text(
                text = order.customerName ?: "Walk-in Customer", 
                style = MaterialTheme.typography.bodyLarge, 
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!order.customerPhone.isNullOrBlank()) {
                Text(
                    text = order.customerPhone,
                    style = MaterialTheme.typography.labelMedium, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.fillMaxHeight())
        OrderTableCell(
            text = "$currencySymbol ${order.total}", 
            weight = 1.2f, 
            textAlign = TextAlign.End, 
            isLast = true,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun RowScope.OrderTableCell(
    text: String,
    weight: Float,
    textAlign: TextAlign = TextAlign.Start,
    isLast: Boolean = false,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    color: Color = Color.Unspecified
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
            style = style,
            textAlign = textAlign,
            color = color
        )
        if (!isLast) {
            VerticalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Preview
@Composable
private fun OrderHistoryScreenPreview() {
    POSOfflineTheme(
        content = {
            OrderHistoryScreen(
                uiState = OrderHistoryUiState(
                    orders = listOf(
                        Order(
                            orderId = 1,
                            customerName = "John Doe",
                            customerPhone = "1234567890",
                            subTotal = 1000.0,
                            discount = 0.0,
                            tax = 0.0,
                            total = 1000.0,
                            paymentMethod = "CASH",
                            paymentStatus = "PAID",
                            totalAmount = 1000.0,
                            createdAt = 1711372800000
                        )
                    )
                ),
                onSearchQueryChange = {},
                onOrderClick = {},
                onDismissDetails = {},
                onLoadNextPage = {},
                onBackClick = {}
            )
        }
    )
}
