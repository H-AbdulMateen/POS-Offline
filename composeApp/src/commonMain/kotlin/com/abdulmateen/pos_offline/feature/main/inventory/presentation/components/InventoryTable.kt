package com.abdulmateen.pos_offline.feature.main.inventory.presentation.components

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.dummyProducts
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.InventoryUiAction
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.models.ProductUi
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.actions
import pos_offline.composeapp.generated.resources.name
import pos_offline.composeapp.generated.resources.quantity
import pos_offline.composeapp.generated.resources.sales_price
import pos_offline.composeapp.generated.resources.sku
import pos_offline.composeapp.generated.resources.stock

@Composable
fun InventoryTable(
    items: List<ProductUi>,
    modifier: Modifier = Modifier,
    uiAction: (InventoryUiAction) -> Unit
) {
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
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(items = items.sortedBy { product -> product.name }) { index,  item ->
                    val swipeState = rememberSwipeableItemState(initialValue = item.isOptionRevealed)
                    SwipeableItemWithActions(
                        state = swipeState,
                        isRevealed = item.isOptionRevealed,
                        onExpanded = { uiAction(InventoryUiAction.ToggleOptionReveal(index, true)) },
                        onCollapsed = {
                            uiAction(InventoryUiAction.ToggleOptionReveal(index, false))
                        },
                        actions = {
                            ActionIcon(
                                onClick = {
                                    uiAction(InventoryUiAction.OnDeleteItemClick(item.productId))
                                },
                                backgroundColor = MaterialTheme.colorScheme.error,
                                icon = Icons.Default.DeleteOutline,
                                modifier = Modifier.fillMaxHeight()
                            )
                            ActionIcon(
                                onClick = {
                                    uiAction(InventoryUiAction.OnEditItemClick(item.productId))
                                },
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                icon = Icons.Default.Edit,
                                modifier = Modifier.fillMaxHeight()
                            )
                            ActionIcon(
                                onClick = {
                                    //TODO
                                },
                                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
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
                                    onClick = { if (!item.isOptionRevealed) swipeState.expand() else swipeState.collapse() },
                                    0.05f
                                )
                            }
                        }
                    }
                }
            }
        }
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
            InventoryTable(
                items = dummyProducts,
                uiAction = {}
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
                items = dummyProducts,
                uiAction = {}
            )
        }
    )
}
