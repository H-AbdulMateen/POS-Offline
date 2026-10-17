package com.abdulmateen.pos_offline.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdulmateen.pos_offline.core.presentation.rememberBitmapFromBytes
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.abs

@Composable
fun ProductListSection(
    modifier: Modifier = Modifier,
    list: List<Product>,
    currencySymbol: String = "$",
    onAddToCart: (Product) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 180.dp),
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = list.sortedBy { item -> item.name },
            key = { item -> item.productId }
        ) { item ->
            ProductCard(
                product = item,
                currencySymbol = currencySymbol,
                onAddToCart = onAddToCart
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    currencySymbol: String = "$",
    onAddToCart: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAddToCart(product) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Product thumbnail box / Initials container
            val bitmap = rememberBitmapFromBytes(product.photoBytes)
            val thumbnailShape = RoundedCornerShape(12.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(thumbnailShape)
                    .background(getPastelColorForProduct(product.name)),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = getInitialsForProduct(product.name),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Product Name
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // SKU
            Text(
                text = product.sku,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom row: Price and Stock status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price
                Text(
                    text = "$currencySymbol${formatPrice(product.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Stock status badge or plain text
                val isOutOfStock = product.quantity <= 0.0
                val isLowStock = product.quantity in 0.1..10.0

                if (isOutOfStock) {
                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "Out of stock",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else if (isLowStock) {
                    Surface(
                        color = Color(0xFFFDF0E6),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "${product.quantity.toInt()} left",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC05621),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Text(
                        text = "${product.quantity.toInt()} in stock",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
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

private fun getInitialsForProduct(name: String): String {
    val words = name.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
    return when {
        words.size >= 2 -> "${words[0].first()}${words[1].first()}".uppercase()
        words.size == 1 -> {
            val w = words[0]
            if (w.length >= 2) w.substring(0, 2).uppercase() else w.uppercase()
        }
        else -> ""
    }
}

private fun getPastelColorForProduct(name: String): Color {
    val hash = abs(name.fold(0) { acc, c -> acc * 31 + c.code })
    val colors = listOf(
        Color(0xFFE2EBF8), // Soft blue
        Color(0xFFEBE6F5), // Soft lavender/purple
        Color(0xFFF7EBE1), // Soft peach/cream
        Color(0xFFE3EDE0), // Soft sage green
        Color(0xFFF7E3E9)  // Soft pink
    )
    return colors[hash % colors.size]
}

val sampleProducts = listOf(
    Product(productId = 1, name = "Linen Tee", sku = "APP-1042", price = 28.00, quantity = 34.0, category = Category(1, "Apparel")),
    Product(productId = 2, name = "Canvas Tote", sku = "ACC-2210", price = 18.00, quantity = 52.0, category = Category(5, "Accessories")),
    Product(productId = 3, name = "Wool Beanie", sku = "APP-1088", price = 24.00, quantity = 12.0, category = Category(1, "Apparel")),
    Product(productId = 4, name = "Ceramic Mug", sku = "HOM-3301", price = 16.00, quantity = 40.0, category = Category(2, "Home")),
    Product(productId = 5, name = "Cedar Soy Candle", sku = "HOM-3315", price = 22.00, quantity = 6.0, category = Category(2, "Home")),
    Product(productId = 6, name = "Cotton Throw", sku = "HOM-3340", price = 64.00, quantity = 9.0, category = Category(2, "Home")),
    Product(productId = 7, name = "Cold Brew Concentrate", sku = "PAN-4102", price = 12.50, quantity = 28.0, category = Category(3, "Pantry")),
    Product(productId = 8, name = "Dark Chocolate Bar", sku = "PAN-4120", price = 4.75, quantity = 96.0, category = Category(3, "Pantry")),
    Product(productId = 9, name = "Maple Granola", sku = "PAN-4131", price = 9.00, quantity = 3.0, category = Category(3, "Pantry")),
    Product(productId = 10, name = "Hand Cream", sku = "BEA-5004", price = 14.00, quantity = 21.0, category = Category(4, "Beauty")),
    Product(productId = 11, name = "Lip Balm", sku = "BEA-5012", price = 5.50, quantity = 70.0, category = Category(4, "Beauty")),
    Product(productId = 12, name = "Oat Bar Soap", sku = "BEA-5020", price = 7.00, quantity = 44.0, category = Category(4, "Beauty")),
    Product(productId = 13, name = "Leather Keyring", sku = "ACC-2231", price = 15.00, quantity = 18.0, category = Category(5, "Accessories")),
    Product(productId = 14, name = "Sunglasses", sku = "ACC-2250", price = 42.00, quantity = 11.0, category = Category(5, "Accessories")),
    Product(productId = 15, name = "Crew Socks", sku = "APP-1101", price = 12.00, quantity = 60.0, category = Category(1, "Apparel")),
    Product(productId = 16, name = "A5 Notebook", sku = "ACC-2274", price = 11.00, quantity = 8.0, category = Category(5, "Accessories"))
)

@Preview(name = "Light Mode")
@Composable
fun ProductListSectionPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            ProductListSection(
                list = sampleProducts,
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
                list = sampleProducts,
                onAddToCart = {}
            )
        }
    )
}
