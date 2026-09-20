package com.abdulmateen.pos_offline.feature.credit.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.feature.credit.presentation.components.ReceivePaymentDialog
import com.abdulmateen.pos_offline.feature.credit.presentation.components.TableCell
import com.abdulmateen.pos_offline.feature.customer.presentation.OrderDetailDialog
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditDetailScreenRoot(
    customerName: String,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<CreditDetailViewModel> { parametersOf(customerName) }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$customerName - Credits") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        CreditDetailScreen(
            uiState = uiState,
            onReceivePayment = viewModel::receivePayment,
            onShowOrderDetails = viewModel::showOrderDetails,
            modifier = Modifier.padding(padding)
        )
    }

    if (uiState.showOrderDetails && uiState.selectedOrder != null) {
        OrderDetailDialog(
            orderWithItems = uiState.selectedOrder!!,
            onDismiss = { viewModel.dismissOrderDetails() },
            businessName = uiState.businessName,
            currencySymbol = uiState.currencySymbol
        )
    }
}

@Composable
fun CreditDetailScreen(
    uiState: CreditDetailUiState,
    onReceivePayment: (CreditEntity, Double) -> Unit,
    onShowOrderDetails: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showReceivePaymentDialog by remember { mutableStateOf<CreditEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    TableCell("Date", 1f, isHeader = true)
                    TableCell("Days", 0.6f, isHeader = true, textAlign = TextAlign.End)
                    TableCell("Total", 1f, isHeader = true, textAlign = TextAlign.End)
                    TableCell("Paid", 1f, isHeader = true, textAlign = TextAlign.End)
                    TableCell("Balance", 1f, isHeader = true, textAlign = TextAlign.End, isLast = true)
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(uiState.credits) { credit ->
                        CreditDetailTableRow(
                            credit = credit,
                            currencySymbol = uiState.currencySymbol,
                            onClick = { 
                                if (credit.orderId != null) {
                                    onShowOrderDetails(credit.orderId)
                                }
                            },
                            onPaymentClick = { showReceivePaymentDialog = credit }
                        )
                        HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            }
        }
    }

    if (showReceivePaymentDialog != null) {
        ReceivePaymentDialog(
            credit = showReceivePaymentDialog!!,
            currencySymbol = uiState.currencySymbol,
            onDismiss = { showReceivePaymentDialog = null },
            onConfirm = { amount ->
                onReceivePayment(showReceivePaymentDialog!!, amount)
                showReceivePaymentDialog = null
            }
        )
    }
}

@Composable
fun CreditDetailTableRow(
    credit: CreditEntity,
    currencySymbol: String,
    onClick: () -> Unit,
    onPaymentClick: () -> Unit
) {
    val date = remember(credit.date) {
        val localDateTime = Instant.fromEpochMilliseconds(credit.date)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
        val year = (localDateTime.year % 100).toString().padStart(2, '0')
        "$day-$month-$year"
    }
    val days = remember(credit.date) {
        val now = Clock.System.now().toEpochMilliseconds()
        val diff = now - credit.date
        (diff / (1000L * 60L * 60L * 24L)).toInt()
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(date, 1f)
        TableCell("$days", 0.6f, textAlign = TextAlign.End)
        TableCell("$currencySymbol${credit.totalAmount}", 1f, textAlign = TextAlign.End)
        TableCell("$currencySymbol${credit.paidAmount}", 1f, textAlign = TextAlign.End)
        Row(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "$currencySymbol${credit.remainingAmount}",
                modifier = Modifier.padding(vertical = 12.dp),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (credit.remainingAmount > 0) MaterialTheme.colorScheme.error else Color(0xFF2E7D32),
                textAlign = TextAlign.End
            )
            if (credit.remainingAmount > 0) {
                IconButton(onClick = onPaymentClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = "Receive Payment",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(32.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
