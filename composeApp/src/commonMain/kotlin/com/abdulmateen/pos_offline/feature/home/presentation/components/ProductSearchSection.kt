package com.abdulmateen.pos_offline.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.barcode_reader
import pos_offline.composeapp.generated.resources.search

@Composable
fun ProductSearchSection(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    onBarcodeScanClick: () -> Unit = {},
    onCustomAmountClick: () -> Unit = {},
    onHeldSalesClick: () -> Unit = {},
    heldSalesCount: Int = 2
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search text field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Search by name or SKU, or scan a barcode",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(Res.string.search),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                IconButton(onClick = onBarcodeScanClick) {
                    Icon(
                        painter = painterResource(Res.drawable.barcode_reader),
                        contentDescription = "Scan barcode",
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            singleLine = true,
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            ),
            modifier = Modifier.weight(1f)
        )



        // Held sales button
//        OutlinedButton(
//            onClick = onHeldSalesClick,
//            shape = CircleShape,
//            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)),
//            colors = ButtonDefaults.outlinedButtonColors(
//                containerColor = MaterialTheme.colorScheme.surface,
//                contentColor = MaterialTheme.colorScheme.onSurface
//            ),
//            modifier = Modifier.height(50.dp)
//        ) {
//            Text(
//                text = if (heldSalesCount > 0) "Held sales ($heldSalesCount)" else "Held sales",
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
    }
}

@Composable
fun CategoryFilterSection(
    modifier: Modifier = Modifier,
    categoryList: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long?) -> Unit
) {
    val displayCategories = if (categoryList.isEmpty()) sampleCategories else categoryList

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "All" chip
        item {
            val isSelected = selectedCategoryId == null
            Surface(
                onClick = { onCategorySelected(null) },
                shape = CircleShape,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.height(38.dp)
            ) {
                Text(
                    text = "All",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }

        // Dynamic category chips
        items(displayCategories, key = { it.categoryId }) { category ->
            val isSelected = selectedCategoryId == category.categoryId
            Surface(
                onClick = { onCategorySelected(category.categoryId) },
                shape = CircleShape,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.height(38.dp)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
    }
}

val sampleCategories = listOf(
    Category(categoryId = 1, name = "Apparel"),
    Category(categoryId = 2, name = "Home"),
    Category(categoryId = 3, name = "Pantry"),
    Category(categoryId = 4, name = "Beauty"),
    Category(categoryId = 5, name = "Accessories")
)

@Preview(name = "Product Search Section")
@Composable
fun ProductSearchSectionPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            ProductSearchSection(
                value = "",
                onValueChange = {}
            )
        }
    )
}

@Preview(name = "Product Search Section Dark")
@Composable
fun ProductSearchSectionPreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            ProductSearchSection(
                value = "",
                onValueChange = {}
            )
        }
    )
}
