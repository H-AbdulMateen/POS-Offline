package com.abdulmateen.pos_offline.feature.main.home.presentation.product_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProductListSection(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(10) {
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
                        Text("Product #$it", fontWeight = FontWeight.Bold)
                        Text("Rs ${(it + 1) * 100}")
                    }
                    Button(onClick = {}) { Text("Add") }
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
            ProductListSection()
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun ProductListSectionPreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            ProductListSection()
        }
    )
}
