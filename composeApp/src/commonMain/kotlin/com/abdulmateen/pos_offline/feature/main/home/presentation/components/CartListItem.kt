package com.abdulmateen.pos_offline.feature.main.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdulmateen.pos_offline.common.presentation.components.ProductPhoto
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.feature.main.home.presentation.order.OrderUiAction
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CartListItem(
    item: CartItem,
    uiAction: (OrderUiAction) -> Unit
) {
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
                    contentDescription = item.productName,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Product Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = item.productName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(text = "Category", color = Color.Gray, fontSize = 13.sp)
                    }
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp).clickable(
                            onClick = {
                                uiAction(OrderUiAction.RemoveCartItem(item.productId))
                            }
                        )
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
                            .border(
                                0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(8.dp)
                            )

                    ) {
                        IconButton(
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                            ).size(24.dp),
                            onClick = {
                                uiAction(OrderUiAction.DecrementInQuantity(item.productId))
                            },
                        ) {
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = "Minus",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = item.quantity.toString().padStart(2, '0'),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            onClick = { uiAction(OrderUiAction.IncrementInQuantity(item.productId)) },
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                                )
                                .size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Plus",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
fun CartListItemLightPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            CartListItem(
                item = CartItem(
                    productId = 1,
                    productName = "Product Name",
                    price = 100.0,
                    quantity = 1.0,
                    unitPrice = 100.0,
                    discount = 100.0,
                    sku = "Sku123",

                    ),
                uiAction = {}
            )
        }
    )
}
@Preview
@Composable
fun CartListItemDarkPreview() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            CartListItem(
                item = CartItem(
                    productId = 1,
                    productName = "Product Name",
                    price = 100.0,
                    quantity = 1.0,
                    unitPrice = 100.0,
                    discount = 100.0,
                    sku = "Sku123",

                    ),
                uiAction = {}
            )
        }
    )
}