package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.SearchField
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.feature.main.inventory.domain.models.InventoryItem
import com.abdulmateen.pos_offline.feature.main.inventory.domain.models.dummyInventory
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.AddEditInventoryDialog
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.InventoryHeaderRow
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.InventoryTable
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.add_item

@Composable
fun InventoryScreen(){
    Scaffold(
    ) { innerPadding ->
        var addEditDialogVisible by remember { mutableStateOf(false) }
        var dialogItem by remember { mutableStateOf<InventoryItem?>(null) }
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
        ) {
            when(deviceConfiguration){
                DeviceConfiguration.MOBILE_PORTRAIT,
                DeviceConfiguration.TABLET_PORTRAIT,
                DeviceConfiguration.DESKTOP-> {
                    InventoryHeaderRow(
                        onAddItemClick = {
                            dialogItem = null
                            addEditDialogVisible = true
                        },
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
                DeviceConfiguration.MOBILE_LANDSCAPE, DeviceConfiguration.TABLET_LANDSCAPE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SearchField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(.1f)
                        )
                        Button(
                            onClick = {
                                dialogItem = null
                                addEditDialogVisible = true
                            },
                            shape = MaterialTheme.shapes.small) {
                            Text(text = stringResource(Res.string.add_item))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    InventoryTable(items = dummyInventory)
                }
            }
        }
        if (addEditDialogVisible){
            AddEditInventoryDialog(
                item = dialogItem,
                onDismiss = { addEditDialogVisible = false },
                onSave = { newItem ->
                    // Handle save or update
                    if (dialogItem == null) {
                        println("Adding item: $newItem")
                    } else {
                        println("Updating item: $newItem")
                    }
                }
            )
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