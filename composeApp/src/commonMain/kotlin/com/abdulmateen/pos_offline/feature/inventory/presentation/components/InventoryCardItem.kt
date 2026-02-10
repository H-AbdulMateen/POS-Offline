package com.abdulmateen.pos_offline.feature.inventory.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.sp
import com.abdulmateen.pos_offline.common.presentation.components.ProductPhoto
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.dummyProducts
import com.abdulmateen.pos_offline.feature.inventory.presentation.models.toProduct
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.quantity_in_stock
import pos_offline.composeapp.generated.resources.stock

@Composable
fun InventoryCardItem(item: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                ProductPhoto(
                    photoBytes = item.photoBytes,
                    contentDescription = item.name,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Product Details
            Column(modifier = Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Category", color = Color.Gray, fontSize = 13.sp)
                    }
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$${item.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    // Quantity Picker
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .border(0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))

                    ) {
                        IconButton(
                            modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)).size(24.dp),
                            onClick = { },
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Minus", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                        }
                        Text(text = item.quantity.toString().padStart(2, '0'), modifier = Modifier.padding(horizontal = 8.dp))
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                                .size(24.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Plus", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}


@Preview(name = "Light Mode")
@Composable
fun InventoryCardItemPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.InventoryCardItem(
                item = dummyProducts[0].toProduct()
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
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.InventoryCardItem(
                item = dummyProducts[0].toProduct()
            )
        }
    )
}