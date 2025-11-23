package com.abdulmateen.pos_offline.feature.main.inventory.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.feature.main.inventory.domain.models.InventoryItem
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.sales_price

@Composable
fun AddEditInventoryDialog(
    item: InventoryItem?,                   // null = Add, not-null = Edit
    onDismiss: () -> Unit,
    onSave: (InventoryItem) -> Unit
) {
    val isEditing = item != null

    var name by remember { mutableStateOf(item?.name ?: "") }
    var sku by remember { mutableStateOf(item?.sku ?: "") }
    var quantity by remember { mutableStateOf(item?.quantity?.toString() ?: "") }
    var salesPrice by remember { mutableStateOf(item?.salesPrice ?: "") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth(0.90f)
        ) {
            Column(Modifier.padding(20.dp)) {

                Text(
                    text = if (isEditing) "Edit Item" else "Add New Item",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = sku,
                    onValueChange = { sku = it },
                    label = { Text("SKU") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { 
                        if (it.all { c -> c.isDigit() }) quantity = it 
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTF(
                    value = salesPrice.toString(),
                    onValueChange = { salesPrice = it },
                    placeholder = stringResource(Res.string.sales_price),
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank() && sku.isNotBlank() && quantity.isNotBlank()) {
                                onSave(
                                    InventoryItem(
                                        name = name,
                                        sku = sku,
                                        quantity = quantity.toInt(),
                                        salesPrice = 0.0,
                                        purchasePrice = 0.0
                                    )
                                )
                                onDismiss()
                            }
                        }
                    ) {
                        Text(if (isEditing) "Update" else "Add")
                    }
                }
            }
        }
    }
}


@Preview(name = "Light Mode")
@Composable
fun AddEditInventoryDialogPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            AddEditInventoryDialog(
                item = null,
                onDismiss = {},
                onSave = {}
            )
        }
    )
}

@Preview(name = "Dark Mode")
@Composable
fun AddEditInventoryDialogPreviewDark(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            AddEditInventoryDialog(
                item = null,
                onDismiss = {},
                onSave = {}
            )
        }
    )
}