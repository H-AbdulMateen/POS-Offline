package com.abdulmateen.pos_offline.feature.inventory.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.dialogs.DestructiveConfirmationDialog
import com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction
import com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.are_you_sure
import pos_offline.composeapp.generated.resources.cancel
import pos_offline.composeapp.generated.resources.delete
import pos_offline.composeapp.generated.resources.do_you_want_to_delete_this_item
import pos_offline.composeapp.generated.resources.name
import pos_offline.composeapp.generated.resources.sales_price
import pos_offline.composeapp.generated.resources.sku
import pos_offline.composeapp.generated.resources.stock

@Composable
fun InventoryTable(
    modifier: Modifier = Modifier,
    uiAction: (InventoryUiAction) -> Unit,
    uiState: InventoryUiState,
    currencySymbol: String = "$",
    onLoadNextPage: () -> Unit = {}
) {
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

    Card(
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Column {
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableHeader(stringResource(Res.string.name), 3.0f, TextAlign.Start)
                TableHeader(stringResource(Res.string.sku), 2.0f, TextAlign.Start)
                TableHeader(stringResource(Res.string.stock), 2.0f, TextAlign.End)
                TableHeader(stringResource(Res.string.sales_price), 2.0f, TextAlign.End)
                TableHeader("Actions", 1.0f, TextAlign.Center)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState
            ) {
                items(
                    items = uiState.productList.sortedBy { product -> product.name },
                    key = { it.productId }
                ) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = androidx.compose.ui.graphics.RectangleShape
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Product Name (3.0f)
                            Box(
                                modifier = Modifier.weight(3.0f).padding(horizontal = 4.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // SKU (2.0f)
                            Box(
                                modifier = Modifier.weight(2.0f).padding(horizontal = 4.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = item.sku.ifEmpty { "-" },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Stock (2.0f)
                            Box(
                                modifier = Modifier.weight(2.0f).padding(horizontal = 4.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                val unitSymbol = item.unit?.symbol ?: "pcs"
                                val isOutOfStock = item.quantity <= 0.0
                                val isLowStock = item.quantity in 0.1..10.0

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = formatQuantity(item.quantity),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isOutOfStock -> MaterialTheme.colorScheme.error
                                            isLowStock -> Color(0xFFC05621)
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                    if (isOutOfStock) {
                                        Text(
                                            text = "Out of stock",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    } else if (isLowStock) {
                                        Text(
                                            text = "Low stock",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFFC05621)
                                        )
                                    }
                                }
                            }

                            // Sales Price (2.0f)
                            Box(
                                modifier = Modifier.weight(2.0f).padding(horizontal = 4.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(
                                    text = "$currencySymbol${formatPrice(item.price)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Actions (1.0f)
                            Box(
                                modifier = Modifier.weight(1.0f).padding(horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            uiAction(InventoryUiAction.ToggleAddEditProductDialog(item.productId))
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    IconButton(
                                        onClick = {
                                            uiAction(InventoryUiAction.ToggleDeleteDialog(item.productId))
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    if (uiState.showDeleteDialog) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.are_you_sure),
            description = stringResource(Res.string.do_you_want_to_delete_this_item),
            confirmButtonText = stringResource(Res.string.delete),
            cancelButtonText = stringResource(Res.string.cancel),
            onDismiss = {
                uiAction(InventoryUiAction.ToggleDeleteDialog(productId = null))
            },
            onCancelClick = {
                uiAction(InventoryUiAction.ToggleDeleteDialog(productId = null))
            },
            onConfirmClick = {
                uiAction(InventoryUiAction.OnDeleteItemClick)
            },
        )
    }
}

@Composable
fun RowScope.TableHeader(
    text: String,
    weight: Float,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = Modifier.weight(weight).padding(horizontal = 4.dp),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

private fun formatPrice(price: Double): String {
    return if (price % 1.0 == 0.0) {
        "${price.toInt()}.00"
    } else {
        val stringVal = ((price * 100).toInt() / 100.0).toString()
        val parts = stringVal.split(".")
        if (parts.size == 2 && parts[1].length == 1) {
            "${stringVal}0"
        } else {
            stringVal
        }
    }
}

private fun formatQuantity(qty: Double): String {
    return if (qty % 1.0 == 0.0) {
        qty.toInt().toString()
    } else {
        qty.toString()
    }
}

@Preview(name = "Light Mode")
@Composable
fun InventoryTablePreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            InventoryTable(
                uiAction = {},
                uiState = InventoryUiState()
            )
        }
    )
}

@Preview(name = "Dark Mode")
@Composable
fun InventoryTablePreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            InventoryTable(
                uiAction = {},
                uiState = InventoryUiState()
            )
        }
    )
}
