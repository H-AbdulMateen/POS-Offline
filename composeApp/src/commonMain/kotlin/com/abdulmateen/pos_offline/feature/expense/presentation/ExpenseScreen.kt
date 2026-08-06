package com.abdulmateen.pos_offline.feature.expense.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.data.database.entities.ExpenseCategory
import com.abdulmateen.pos_offline.data.database.entities.ExpenseEntity
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
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
        onAddRevenue = viewModel::addRevenue
    )
}

@Composable
fun ExpenseScreen(
    uiState: ExpenseUiState,
    onAddExpense: (Double, String, ExpenseCategory, Long?) -> Unit,
    onAddRevenue: (Double, String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showAddRevenueDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.End
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
            
            Text(text = "Month: ${uiState.selectedMonth.month} ${uiState.selectedMonth.year}")
            
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
            
            Text(text = "Recent Expenses", style = MaterialTheme.typography.titleMedium)
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.recentExpenses) { expense ->
                    ExpenseItem(expense)
                }
            }
        }
    }

    if (showAddDialog) {
        AddExpenseDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { amount, desc, category ->
                onAddExpense(amount, desc, category, null)
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
fun ExpenseItem(expense: ExpenseEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = expense.description, fontWeight = FontWeight.Medium)
                Text(text = expense.category.name, style = MaterialTheme.typography.labelSmall)
            }
            Text(text = "Rs ${expense.amount}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, String, ExpenseCategory) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ExpenseCategory.OTHER) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                onConfirm(amountDouble, description, category)
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
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Category")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ExpenseCategory.entries.forEach {
                        FilterChip(
                            selected = category == it,
                            onClick = { category = it },
                            label = { Text(it.name) }
                        )
                    }
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
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth()
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
                            category = ExpenseCategory.OTHER
                        ),
                        ExpenseEntity(
                            expenseId = 2,
                            amount = 5000.0,
                            description = "Internet Bill",
                            category = ExpenseCategory.BILL
                        )
                    )
                ),
                onAddExpense = { _, _, _, _ -> },
                onAddRevenue = { _, _ -> }
            )
        }
    )
}
