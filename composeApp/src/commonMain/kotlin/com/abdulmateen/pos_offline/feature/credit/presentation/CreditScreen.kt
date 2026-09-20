package com.abdulmateen.pos_offline.feature.credit.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.feature.credit.presentation.components.TableCell
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreditScreenRoot(
    onCustomerClick: (String) -> Unit
) {
    val viewModel = koinViewModel<CreditViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    CreditScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onExportReport = viewModel::exportReport,
        onCustomerClick = onCustomerClick
    )
}

@Composable
fun CreditScreen(
    uiState: CreditUiState,
    onSearchQueryChange: (String) -> Unit,
    onExportReport: () -> Unit,
    onCustomerClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Credit Management",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onExportReport) {
                    Icon(Icons.Default.FileDownload, contentDescription = "Export CSV")
                }
            }

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
                        TableCell("Oldest Date", 1f, isHeader = true)
                        TableCell("Customer", 1.8f, isHeader = true)
                        TableCell("Days", 0.7f, isHeader = true, textAlign = TextAlign.End)
                        TableCell("Total (${uiState.currencySymbol})", 1f, isHeader = true, textAlign = TextAlign.End)
                        TableCell("Paid (${uiState.currencySymbol})", 1f, isHeader = true, textAlign = TextAlign.End)
                        TableCell("Balance (${uiState.currencySymbol})", 1f, isHeader = true, textAlign = TextAlign.End, isLast = true)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.summaries) { summary ->
                            CreditSummaryTableRow(
                                summary = summary,
                                currencySymbol = uiState.currencySymbol,
                                onClick = { onCustomerClick(summary.customerName) }
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                        }
                        
                        item {
                            if (uiState.isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreditSummaryTableRow(summary: CreditSummary, currencySymbol: String, onClick: () -> Unit) {
    val date = remember(summary.oldestDate) {
        val localDateTime = Instant.fromEpochMilliseconds(summary.oldestDate)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
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
        TableCell(summary.customerName, 1.8f)
        TableCell("${summary.noOfDays}", 0.7f, textAlign = TextAlign.End)
        TableCell("$currencySymbol ${summary.totalAmount}", 1f, textAlign = TextAlign.End)
        TableCell("$currencySymbol ${summary.paidAmount}", 1f, textAlign = TextAlign.End)
        TableCell("$currencySymbol ${summary.remainingAmount}", 1f, textAlign = TextAlign.End, isLast = true)
    }
}

@Preview
@Composable
private fun CreditScreenPreview() {
    POSOfflineTheme(
        content = {
            CreditScreen(
                uiState = CreditUiState(
                    summaries = listOf(
                        CreditSummary(
                            customerName = "John Doe",
                            phoneNumber = "123",
                            totalAmount = 5000.0,
                            paidAmount = 2000.0,
                            remainingAmount = 3000.0,
                            oldestDate = 123456789,
                            noOfDays = 5
                        )
                    )
                ),
                onSearchQueryChange = {},
                onExportReport = {},
                onCustomerClick = {}
            )
        }
    )
}
