package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.SearchField
import com.abdulmateen.pos_offline.feature.main.inventory.domain.models.dummyInventory
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.InventoryHeaderRow
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.InventoryTable
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InventoryScreen(){
    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
        ) {
            InventoryHeaderRow(
                onAddItemClick = {},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            SearchField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            InventoryTable(items = dummyInventory)
        }

    }
}

@Preview(name = "Light Mode")
@Composable
fun InventoryScreenPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            InventoryScreen()
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun InventoryScreenPreviewDark(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            InventoryScreen()
        }
    )
}