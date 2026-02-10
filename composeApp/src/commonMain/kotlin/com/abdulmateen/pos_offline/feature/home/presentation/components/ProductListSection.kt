package com.abdulmateen.pos_offline.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.dummyProducts
import com.abdulmateen.pos_offline.feature.inventory.presentation.models.toProduct
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.add

@Composable
fun ProductListSection(
    modifier: Modifier = Modifier,
    list: List<Product>,
    onAddToCart: (Product) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = list.sortedBy{item -> item.name}, key = { item -> item.productId }) {item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text("Rs ${item.price}")
                    }
                    Button(
                        onClick = { onAddToCart(item) }
                    ) {
                        Text(text = stringResource(Res.string.add))
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode")
@Composable
fun ProductListSectionPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            ProductListSection(
                list = dummyProducts.map { it.toProduct() },
                onAddToCart = {}
            )
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun ProductListSectionPreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            ProductListSection(
                list = dummyProducts.map { it.toProduct() },
                onAddToCart = {}
            )
        }
    )
}
