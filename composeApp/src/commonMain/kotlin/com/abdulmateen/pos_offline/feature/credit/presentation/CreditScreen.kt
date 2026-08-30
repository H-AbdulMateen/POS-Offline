package com.abdulmateen.pos_offline.feature.credit.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.data.database.entities.CreditEntity
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun CreditScreenRoot() {
    val viewModel = koinViewModel<CreditViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    CreditScreen(
        uiState = uiState,
        onReceivePayment = viewModel::receivePayment,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onAddCredit = viewModel::addCredit
    )
}

@Composable
fun CreditScreen(
    uiState: CreditUiState,
    onReceivePayment: (CreditEntity, Double) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddCredit: (String, String?, Double, Double) -> Unit
) {
    var showReceivePaymentDialog by remember { mutableStateOf<CreditEntity?>(null) }
    var showAddCreditDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddCreditDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Credit") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Credit Management",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearchQueryChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by customer name...") },
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell("Date", 1f, isHeader = true)
                        TableCell("Customer", 2f, isHeader = true)
                        TableCell("Phone", 1.5f, isHeader = true)
                        TableCell("Total", 1.2f, isHeader = true, textAlign = TextAlign.End)
                        TableCell("Remaining", 1.2f, isHeader = true, textAlign = TextAlign.End, isLast = true)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.credits) { credit ->
                            CreditTableRow(
                                credit = credit,
                                onClick = { showReceivePaymentDialog = credit }
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }
                }
            }
        }
    }

    if (showReceivePaymentDialog != null) {
        ReceivePaymentDialog(
            credit = showReceivePaymentDialog!!,
            onDismiss = { showReceivePaymentDialog = null },
            onConfirm = { amount ->
                onReceivePayment(showReceivePaymentDialog!!, amount)
                showReceivePaymentDialog = null
            }
        )
    }

    if (showAddCreditDialog) {
        AddCreditDialog(
            onDismiss = { showAddCreditDialog = false },
            onConfirm = { name, phone, total, paid ->
                onAddCredit(name, phone, total, paid)
                showAddCreditDialog = false
            }
        )
    }
}

@Composable
fun CreditTableRow(credit: CreditEntity, onClick: () -> Unit) {
    val date = remember(credit.date) {
        val localDateTime = Instant.fromEpochMilliseconds(credit.date)
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
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(date, 1f)
        TableCell(credit.customerName, 2f)
        TableCell(credit.phoneNumber ?: "-", 1.5f)
        TableCell("Rs ${credit.totalAmount}", 1.2f, textAlign = TextAlign.End)
        TableCell("Rs ${credit.remainingAmount}", 1.2f, textAlign = TextAlign.End, isLast = true)
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false,
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
            style = if (isHeader) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodySmall,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            textAlign = textAlign,
            color = if (!isHeader && text.startsWith("Rs") && !text.contains("Total")) {
                 if ((text.replace("Rs ", "").toDoubleOrNull() ?: 0.0) > 0) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)
            } else Color.Unspecified
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
fun ReceivePaymentDialog(
    credit: CreditEntity,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                onConfirm(amountDouble)
            }) {
                Text("Receive")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Receive Payment") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Customer: ${credit.customerName}")
                Text("Remaining: Rs ${credit.remainingAmount}", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = amount,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.toDoubleOrNull() != null || (input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' })) {
                            amount = input
                        }
                    },
                    label = { Text("Payment Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal)
                )
            }
        }
    )
}

@Composable
fun AddCreditDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String?, Double, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }
    var paid by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val totalD = total.toDoubleOrNull() ?: 0.0
                val paidD = paid.toDoubleOrNull() ?: 0.0
                onConfirm(name, phone.takeIf { it.isNotBlank() }, totalD, paidD)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add New Credit") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Customer Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '+' || it == '-' }) {
                            phone = input
                        }
                    },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone)
                )
                OutlinedTextField(
                    value = total,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.toDoubleOrNull() != null || (input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' })) {
                            total = input
                        }
                    },
                    label = { Text("Total Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = paid,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.toDoubleOrNull() != null || (input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' })) {
                            paid = input
                        }
                    },
                    label = { Text("Paid Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal)
                )
            }
        }
    )
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun CreditScreenPreview() {
    POSOfflineTheme(
        content = {
            CreditScreen(
                uiState = CreditUiState(
                    credits = listOf(
                        CreditEntity(
                            customerName = "John Doe",
                            totalAmount = 5000.0,
                            paidAmount = 2000.0,
                            remainingAmount = 3000.0
                        ),
                        CreditEntity(
                            customerName = "Jane Smith",
                            totalAmount = 10000.0,
                            paidAmount = 10000.0,
                            remainingAmount = 0.0
                        )
                    )
                ),
                onReceivePayment = { _, _ -> },
                onSearchQueryChange = {},
                onAddCredit = { _, _, _, _ -> }
            )
        }
    )
}
