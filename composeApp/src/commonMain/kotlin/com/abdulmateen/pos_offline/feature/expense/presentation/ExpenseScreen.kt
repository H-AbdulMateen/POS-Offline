package com.abdulmateen.pos_offline.feature.expense.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.data.database.entities.ExpenseCategory
import com.abdulmateen.pos_offline.data.database.entities.ExpenseEntity
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun ExpenseScreenRoot() {
    val viewModel = koinViewModel<ExpenseViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ExpenseScreen(
        uiState = uiState,
        onAddExpense = viewModel::addExpense,
        onAddRevenue = viewModel::addRevenue,
        onPreviousMonth = viewModel::previousMonth,
        onNextMonth = viewModel::nextMonth
    )
}

@Composable
fun ExpenseScreen(
    uiState: ExpenseUiState,
    onAddExpense: (Double, String, ExpenseCategory, String?, Long?) -> Unit,
    onAddRevenue: (Double, String) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showAddRevenueDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                ExtendedFloatingActionButton(
                    onClick = { showAddRevenueDialog = true },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add Revenue") }
                )
                ExtendedFloatingActionButton(
                    onClick = { showAddDialog = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add Expense") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Expense Management",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onPreviousMonth) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                }
                Text(
                    text = "${uiState.selectedMonth.month} ${uiState.selectedMonth.year}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                IconButton(onClick = onNextMonth) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.profitLoss >= 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Monthly Summary", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Revenue", style = MaterialTheme.typography.bodySmall)
                            Text("Rs ${uiState.totalRevenue}", fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Expenses", style = MaterialTheme.typography.bodySmall)
                            Text("Rs ${uiState.totalExpenses}", fontWeight = FontWeight.Bold)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Profit/Loss", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Rs ${uiState.profitLoss}",
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.profitLoss >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(text = "Expense Records", style = MaterialTheme.typography.titleMedium)
            
            Spacer(modifier = Modifier.height(8.dp))

            // Table Container
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
                        TableHeaderCell("Date", 1f)
                        TableHeaderCell("Paid To", 2f)
                        TableHeaderCell("Category", 1.5f)
                        TableHeaderCell("Amount", 1.2f, isLast = true, textAlign = TextAlign.End)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        items(uiState.recentExpenses) { expense ->
                            ExpenseTableRow(expense)
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        }
                        
                        if (uiState.recentExpenses.isEmpty()) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                    Text(text = "No expenses recorded for this month", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }

            // Total Monthly Expense Footer
            if (uiState.recentExpenses.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Monthly Expense",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Rs ${uiState.totalExpenses}",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddExpenseDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { amount, desc, category, paidTo ->
                onAddExpense(amount, desc, category, paidTo, null)
                showAddDialog = false
            }
        )
    }

    if (showAddRevenueDialog) {
        AddRevenueDialog(
            onDismiss = { showAddRevenueDialog = false },
            onConfirm = { amount, desc ->
                onAddRevenue(amount, desc)
                showAddRevenueDialog = false
            }
        )
    }
}

@Composable
fun RowScope.TableHeaderCell(
    text: String,
    weight: Float,
    isLast: Boolean = false,
    textAlign: TextAlign = TextAlign.Start
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
fun ExpenseTableRow(expense: ExpenseEntity) {
    val date = remember(expense.date) {
        val localDateTime = kotlin.time.Instant.fromEpochMilliseconds(expense.date)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.day.toString().padStart(2, '0')
        val month = localDateTime.month.number.toString().padStart(2, '0')
        val year = (localDateTime.year % 100).toString().padStart(2, '0')
        "$day-$month-$year"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(date, 1f)
        TableCell(expense.paidTo ?: "-", 2f)
        TableCell(expense.category.name, 1.5f)
        TableCell("${expense.amount}", 1.2f, isLast = true, textAlign = TextAlign.End)
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    isLast: Boolean = false,
    textAlign: TextAlign = TextAlign.Start
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

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, String, ExpenseCategory, String?) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var paidTo by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ExpenseCategory.OTHER) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                onConfirm(amountDouble, description, category, paidTo)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.toDoubleOrNull() != null || (input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' })) {
                            amount = input
                        }
                    },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = paidTo,
                    onValueChange = { paidTo = it },
                    label = { Text("Paid To") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = category.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        ExpenseCategory.entries.forEach { entry ->
                            DropdownMenuItem(
                                text = { Text(entry.name) },
                                onClick = {
                                    category = entry
                                    expanded = false
                                }
                            )
                        }
                    }
                    // Overlay a clickable box to trigger dropdown
                    Box(modifier = Modifier.matchParentSize().clickable { expanded = !expanded })
                }
            }
        }
    )
}

@Composable
fun AddRevenueDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                onConfirm(amountDouble, description)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add Manual Revenue") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.toDoubleOrNull() != null || (input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' })) {
                            amount = input
                        }
                    },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Source/Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun ExpenseScreenPreview() {
    POSOfflineTheme(
        content = {
            ExpenseScreen(
                uiState = ExpenseUiState(
                    totalRevenue = 50000.0,
                    totalExpenses = 15000.0,
                    profitLoss = 35000.0,
                    recentExpenses = listOf(
                        ExpenseEntity(
                            expenseId = 1,
                            amount = 2000.0,
                            description = "Office Supplies",
                            category = ExpenseCategory.OTHER,
                            paidTo = "Local Store"
                        ),
                        ExpenseEntity(
                            expenseId = 2,
                            amount = 5000.0,
                            description = "Internet Bill",
                            category = ExpenseCategory.BILL,
                            paidTo = "ISP Provider"
                        )
                    )
                ),
                onAddExpense = { _, _, _, _, _ -> },
                onAddRevenue = { _, _ -> },
                onPreviousMonth = {},
                onNextMonth = {}
            )
        }
    )
}
