package com.abdulmateen.pos_offline.feature.reports.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.WheelDateTimePickerDialog
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportsScreenRoot() {
    val viewModel = koinViewModel<ReportsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ReportsScreen(
        uiState = uiState,
        onExportSales = viewModel::exportSales,
        onExportExpenses = viewModel::exportExpenses,
        onClearMessage = viewModel::clearMessage,
        onStartDateChange = viewModel::updateStartDate,
        onEndDateChange = viewModel::updateEndDate
    )
}

@Composable
fun ReportsScreen(
    uiState: ReportsUiState,
    onExportSales: () -> Unit,
    onExportExpenses: () -> Unit,
    onClearMessage: () -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.exportMessage) {
        uiState.exportMessage?.let {
            snackbarHostState.showSnackbar(it)
            onClearMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Reports & Exports",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Text(
                text = "Generate and download your business reports in CSV format. Range is limited to 30 days for optimal performance.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Export Date Range", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showStartDatePicker = true },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("From", style = MaterialTheme.typography.labelSmall)
                                Text(uiState.startDate.toString(), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        OutlinedButton(
                            onClick = { showEndDatePicker = true },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("To", style = MaterialTheme.typography.labelSmall)
                                Text(uiState.endDate.toString(), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ReportCard(
                title = "Sales Report",
                description = "Export all orders with details like total, customer, and payment method.",
                onExport = onExportSales,
                isLoading = uiState.isLoading
            )

            ReportCard(
                title = "Expense Report",
                description = "Export all recorded expenses with categories and payment details.",
                onExport = onExportExpenses,
                isLoading = uiState.isLoading
            )
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }

    WheelDateTimePickerDialog(
        showDatePicker = showStartDatePicker,
        toggleDatePicker = { showStartDatePicker = it },
        onDateSelection = { instant ->
            onStartDateChange(instant.toLocalDateTime(TimeZone.currentSystemDefault()).date)
        }
    )

    WheelDateTimePickerDialog(
        showDatePicker = showEndDatePicker,
        toggleDatePicker = { showEndDatePicker = it },
        onDateSelection = { instant ->
            onEndDateChange(instant.toLocalDateTime(TimeZone.currentSystemDefault()).date)
        }
    )
}

@Composable
fun ReportCard(
    title: String,
    description: String,
    onExport: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            Button(
                onClick = onExport,
                enabled = !isLoading,
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export CSV")
            }
        }
    }
}

@Preview
@Composable
private fun ReportsScreenPreview() {
    POSOfflineTheme(
        content = {
            ReportsScreen(
                uiState = ReportsUiState(),
                onExportSales = {},
                onExportExpenses = {},
                onClearMessage = {},
                onStartDateChange = {},
                onEndDateChange = {}
            )
        }
    )
}
