package com.abdulmateen.pos_offline.feature.main.inventory.presentation.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.abdulmateen.pos_offline.common.presentation.components.ProductPhoto
import com.abdulmateen.pos_offline.domain.models.ProductDetail
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.models.ProductUi
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.book_error
import pos_offline.composeapp.generated.resources.stock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ProductDetailDialog(
    product: ProductDetail,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                ProductPhoto(
                    photoBytes = product.photoBytes,
                    contentDescription = product.name,
                    modifier = Modifier.size(120.dp)
                        .align(Alignment.CenterHorizontally)
                )


                        Spacer(Modifier.height(8.dp))

                        // Title & Category
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = if (product.category != null) product.category.name else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(8.dp))

                        // Price Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${product.price}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                        // Quantity Selector
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                text = stringResource(Res.string.stock).plus(": ").plus(product.stock.toString()),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )

                        }

                        Spacer(Modifier.height(24.dp))

            }
        }
    }
}


@Preview(name = "Light Mode")
@Composable
fun ProductDetailDialogPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            ProductDetailDialog(
                product = ProductDetail(
                    name = "Book",
                    sku = "123456",
                    barcode = "123456789",
                    purchasePrice = 100.0,
                    price = 200.0,
                    stock = 10.0,
                    photoBytes = null,
                    category = null,
                    unit = null
                ),
                onDismiss = {}
            )
        }
    )
}

@Preview(name = "Dark Mode")
@Composable
fun ProductDetailDialogPreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            ProductDetailDialog(
                product = ProductDetail(
                    name = "Book",
                    sku = "123456",
                    barcode = "123456789",
                    purchasePrice = 100.0,
                    price = 200.0,
                    stock = 10.0,
                    photoBytes = null,
                    category = null,
                    unit = null
                ),
                onDismiss = {}
            )
        }
    )
}
