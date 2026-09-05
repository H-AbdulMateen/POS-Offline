package com.abdulmateen.pos_offline.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.feature.home.presentation.utils.generateInvoiceInPdf
import com.abdulmateen.pos_offline.feature.home.presentation.utils.isPrinterAvailable
import com.abdulmateen.pos_offline.feature.home.presentation.utils.printPdf
import com.abdulmateen.pos_offline.feature.home.presentation.utils.saveInvoiceFile
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.round

@OptIn(ExperimentalTime::class)
@Composable
fun PaymentDialog(
    cartItems: List<CartItem>,
    subTotal: Double,
    discount: Double,
    tax: Double,
    totalAmount: Double,
    businessName: String,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (String?, String?) -> Unit,
    onConfirmCredit: (String, String?, Double) -> Unit
) {
    var paymentType by remember { mutableStateOf("Cash") }
    var paidAmount by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showNoPrinterDialog by remember { mutableStateOf(false) }

    if (showNoPrinterDialog) {
        AlertDialog(
            onDismissRequest = { showNoPrinterDialog = false },
            confirmButton = {
                TextButton(onClick = { showNoPrinterDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("No Printer Found") },
            text = { Text("There is no printer available to print the invoice.") }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (paymentType == "Credit") {
                    Button(
                        onClick = {
                            val paid = paidAmount.toDoubleOrNull() ?: 0.0
                            onConfirmCredit(customerName, phoneNumber.takeIf { it.isNotBlank() }, paid)
                        },
                        enabled = customerName.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirm Credit")
                    }
                } else {
                    Button(
                        onClick = {
                            val paid = paidAmount.toDoubleOrNull() ?: 0.0
                            val change = (paid - totalAmount).coerceAtLeast(0.0)
                            val fileName = "invoice_${Clock.System.now().toEpochMilliseconds()}.pdf"
                            val invoiceData = generateInvoiceInPdf(
                                cartItems = cartItems,
                                subTotal = subTotal,
                                discount = discount,
                                tax = tax,
                                total = totalAmount,
                                paidAmount = paid,
                                change = change,
                                paymentType = paymentType,
                                businessName = businessName,
                                currencySymbol = currencySymbol
                            )
                            saveInvoiceFile(invoiceData, fileName)
                            onConfirm(customerName.takeIf { it.isNotBlank() }, phoneNumber.takeIf { it.isNotBlank() })
                        },
                        enabled = paidAmount.toDoubleOrNull()?.let { it >= totalAmount } ?: false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirm & View")
                    }
                    Button(
                        onClick = {
                            if (isPrinterAvailable()) {
                                val paid = paidAmount.toDoubleOrNull() ?: 0.0
                                val change = (paid - totalAmount).coerceAtLeast(0.0)
                                val fileName = "invoice_${Clock.System.now().toEpochMilliseconds()}.pdf"
                                val invoiceData = generateInvoiceInPdf(
                                    cartItems = cartItems,
                                    subTotal = subTotal,
                                    discount = discount,
                                    tax = tax,
                                    total = totalAmount,
                                    paidAmount = paid,
                                    change = change,
                                    paymentType = paymentType,
                                    businessName = businessName,
                                    currencySymbol = currencySymbol
                                )
                                printPdf(invoiceData, fileName)
                                onConfirm(customerName.takeIf { it.isNotBlank() }, phoneNumber.takeIf { it.isNotBlank() })
                            } else {
                                showNoPrinterDialog = true
                            }
                        },
                        enabled = paidAmount.toDoubleOrNull()?.let { it >= totalAmount } ?: false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirm & Print")
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Proceed to Payment") },
        text = {
            Column {
                Text("Total: $currencySymbol $totalAmount", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Select Payment Type")
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Cash", "Card", "Credit").forEach {
                        FilterChip(
                            selected = paymentType == it,
                            onClick = { paymentType = it },
                            label = { Text(it) }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Customer Name (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '+' || it == '-' }) {
                            phoneNumber = input
                        }
                    },
                    label = { Text("Phone Number (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = paidAmount,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.toDoubleOrNull() != null || (input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' })) {
                            paidAmount = input
                        }
                    },
                    label = { Text("Amount Paid") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                if (paidAmount.isNotEmpty()) {
                    val change = paidAmount.toDoubleOrNull()?.minus(totalAmount)
                    if (change != null) {
                        if (change >= 0) {
                            Text("Change: $currencySymbol ${round(change * 100) / 100.0}")
                        } else {
                            Text("Remaining (Credit): $currencySymbol ${round(-change * 100) / 100.0}", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    )
}

@Preview(name = "Payment Dialog")
@Composable
fun PaymentDialogPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            PaymentDialog(
                cartItems = emptyList(),
                subTotal = 100.0,
                discount = 0.0,
                tax = 0.0,
                totalAmount = 100.0,
                businessName = "My Business",
                currencySymbol = "$",
                onDismiss = {},
                onConfirm = { _, _ -> },
                onConfirmCredit = { _, _, _ -> }
            )
        }
    )
}
@Preview(name = "Payment Dialog Dark")
@Composable
fun PaymentDialogPreviewDark(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            PaymentDialog(
                cartItems = emptyList(),
                subTotal = 100.0,
                discount = 0.0,
                tax = 0.0,
                totalAmount = 100.0,
                businessName = "My Business",
                currencySymbol = "$",
                onDismiss = {},
                onConfirm = { _, _ -> },
                onConfirmCredit = { _, _, _ -> }
            )
        }
    )
}
