package com.abdulmateen.pos_offline.feature.main.inventory.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.models.dummyProducts
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.name
import pos_offline.composeapp.generated.resources.quantity
import pos_offline.composeapp.generated.resources.sales_price
import pos_offline.composeapp.generated.resources.sku

@Composable
fun InventoryTable(
    items: List<Product>,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp)) {
                TableHeader(stringResource(Res.string.name), 0.25f)
                TableHeader(stringResource(Res.string.sku), 0.15f)
                TableHeader(stringResource(Res.string.quantity), 0.15f)
                TableHeader(stringResource(Res.string.sales_price), 0.15f)
            }

            HorizontalDivider()

            LazyColumn {
                items(items) { item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 4.dp)
                    ) {
                        TableCell(item.name, 0.25f)
                        TableCell(item.sku, 0.15f)
                        TableCell(item.quantity.toString(), 0.15f)
                        TableCell(item.salePrice.toString(), 0.15f)
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

@Preview(name = "Light Mode")
@Composable
fun InventoryTablePreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            InventoryTable(
                items = dummyProducts
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
                items = dummyProducts
            )
        }
    )
}
