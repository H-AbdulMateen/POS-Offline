package com.abdulmateen.pos_offline.feature.main.home.presentation.product_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PaymentDialog(
    totalAmount: Double,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var paymentType by remember { mutableStateOf("Cash") }
    var paidAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(paymentType) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm Payment")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Proceed to Payment") },
        text = {
            Column {
                Text("Total: Rs $totalAmount", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Select Payment Type")
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Cash", "Card", "Split").forEach {
                        FilterChip(
                            selected = paymentType == it,
                            onClick = { paymentType = it },
                            label = { Text(it) }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = paidAmount,
                    onValueChange = { paidAmount = it },
                    label = { Text("Amount Paid") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (paidAmount.isNotEmpty()) {
                    val change = paidAmount.toDoubleOrNull()?.minus(totalAmount)
                    if (change != null && change >= 0)
                        Text("Change: Rs %.2f$change")
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
                totalAmount = 100.0,
                onDismiss = {},
                onConfirm = {}
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
                totalAmount = 100.0,
                onDismiss = {},
                onConfirm = {}
            )
        }
    )
}