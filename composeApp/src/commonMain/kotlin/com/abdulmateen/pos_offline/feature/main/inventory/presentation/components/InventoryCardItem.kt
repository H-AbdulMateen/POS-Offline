package com.abdulmateen.pos_offline.feature.main.inventory.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.dummyProducts
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InventoryCardItem(item: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(item.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))

            Text("SKU: ${item.sku}")
            Text("Qty: ${item.quantity}")
            Text("Location: ${item.price}")
        }
    }
}


@Preview(name = "Light Mode")
@Composable
fun InventoryCardItemPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            InventoryCardItem(
                item = dummyProducts[0]
            )
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun InventoryCardItemPreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            InventoryCardItem(
                item = dummyProducts[0]
            )
        }
    )
}