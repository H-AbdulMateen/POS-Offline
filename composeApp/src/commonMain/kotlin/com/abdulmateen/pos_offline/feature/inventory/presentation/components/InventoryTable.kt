package com.abdulmateen.pos_offline.feature.inventory.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.dialogs.DestructiveConfirmationDialog
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.dummyProducts
import com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction
import com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState
import com.abdulmateen.pos_offline.feature.inventory.presentation.components.IconTableCell
import com.abdulmateen.pos_offline.feature.inventory.presentation.components.TableCell
import com.abdulmateen.pos_offline.feature.inventory.presentation.components.TableHeader
import com.abdulmateen.pos_offline.feature.inventory.presentation.models.ProductUi
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.actions
import pos_offline.composeapp.generated.resources.are_you_sure
import pos_offline.composeapp.generated.resources.cancel
import pos_offline.composeapp.generated.resources.delete
import pos_offline.composeapp.generated.resources.do_you_want_to_delete_this_item
import pos_offline.composeapp.generated.resources.name
import pos_offline.composeapp.generated.resources.quantity
import pos_offline.composeapp.generated.resources.sales_price
import pos_offline.composeapp.generated.resources.sku
import pos_offline.composeapp.generated.resources.stock

@Composable
fun InventoryTable(
    modifier: Modifier = Modifier,
    uiAction: (com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction) -> Unit,
    uiState: com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState,
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
            ) {
                TableHeader(stringResource(Res.string.name), 0.35f)
                TableHeader(stringResource(Res.string.stock), 0.15f)
                TableHeader(stringResource(Res.string.sales_price), 0.15f)
                TableHeader("", 0.05f)
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = uiState.productList.sortedBy { product -> product.name }, key = { it.productId }) { item ->
                    _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.SwipeableItemWithActions(
                        isRevealed = item.isOptionRevealed,
                        onExpanded = {
                            uiAction(
                                _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleOptionReveal(
                                    item.productId,
                                    true
                                )
                            )
                        },
                        onCollapsed = {
                            uiAction(
                                _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleOptionReveal(
                                    item.productId,
                                    false
                                )
                            )
                        },
                        actions = {
                            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.ActionIcon(
                                onClick = {
                                    uiAction(
                                        _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleOptionReveal(
                                            item.productId,
                                            false
                                        )
                                    )
                                    uiAction(
                                        _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleDeleteDialog(
                                            productId = item.productId
                                        )
                                    )
                                },
                                backgroundColor = MaterialTheme.colorScheme.error,
                                icon = Icons.Default.DeleteOutline,
                                modifier = Modifier.fillMaxHeight()
                            )
                            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.ActionIcon(
                                onClick = {
                                    uiAction(
                                        _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleAddEditProductDialog(
                                            productId = item.productId
                                        )
                                    )
                                },
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                icon = Icons.Default.Edit,
                                modifier = Modifier.fillMaxHeight()
                            )
                            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.ActionIcon(
                                onClick = {
                                    uiAction(
                                        _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleDetailDialog(
                                            productId = item.productId
                                        )
                                    )
                                },
                                backgroundColor = MaterialTheme.colorScheme.primaryFixed,
                                icon = Icons.Default.Preview,
                                modifier = Modifier.fillMaxHeight()
                            )
                        },
                    ) {
                        Card {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 4.dp)
                            ) {
                                TableCell(item.name, 0.35f)
                                TableCell(item.quantity.toString(), 0.15f)
                                TableCell(item.price.toString(), 0.15f)
                                IconTableCell(
                                    onClick = {
                                        if (!item.isOptionRevealed) {
                                            uiAction(
                                                _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleOptionReveal(
                                                    productId = item.productId,
                                                    isRevealed = true
                                                )
                                            )
                                        } else {
                                            uiAction(
                                                _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleOptionReveal(
                                                    productId = item.productId,
                                                    isRevealed = false
                                                )
                                            )
                                        }
                                    },
                                    0.05f
                                )
                            }
                        }
                    }
                }
                item {
                    if (uiState.isLoading) {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
    if (uiState.showDeleteDialog){
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.are_you_sure),
            description = stringResource(Res.string.do_you_want_to_delete_this_item),
            confirmButtonText = stringResource(Res.string.delete),
            cancelButtonText = stringResource(Res.string.cancel),
            onDismiss = {
                uiAction(_root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleDeleteDialog(productId = null))
            },
            onCancelClick = {
                uiAction(_root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.ToggleDeleteDialog(productId = null))
            },
            onConfirmClick = {
                uiAction(_root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.OnDeleteItemClick)
            },
        )
    }
}

@Composable
fun RowScope.TableHeader(text: String, weight: Float) {
    Text(
        text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
fun RowScope.TableCell(text: String, weight: Float) {
    Text(
        text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun RowScope.IconTableCell(onClick: () -> Unit, weight: Float) {
    Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = stringResource(Res.string.actions),
        modifier = Modifier.clickable(
            onClick = onClick
        )

    )
}

@Preview(name = "Light Mode")
@Composable
fun InventoryTablePreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.InventoryTable(
                uiAction = {},
                uiState = _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState()
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
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.InventoryTable(
                uiAction = {},
                uiState = _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState()
            )
        }
    )
}
