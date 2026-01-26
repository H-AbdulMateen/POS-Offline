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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.SearchField
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.InventoryHeaderRow
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.InventoryTable
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.dialogs.AddEditInventoryDialog
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.models.ProductUi
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.add_item

@Composable
fun InventoryScreenRoot(){
    val viewModel = koinViewModel<InventoryViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val searchProductQuery = viewModel.searchProductQuery.collectAsStateWithLifecycle().value
    val event = viewModel.eventChannel
    InventoryScreen(
        uiState = uiState,
        uiAction = viewModel::uiAction,
        onSearchProductQueryChange = viewModel::onSearchProductQueryChange,
        searchProductQuery = searchProductQuery,
        eventChannel = event
    )
}


@Composable
fun InventoryScreen(
    searchProductQuery: String,
    uiState: InventoryUiState = InventoryUiState(),
    onSearchProductQueryChange: (String) -> Unit,
    uiAction: (InventoryUiAction) -> Unit,
    eventChannel: Flow<InventoryEvents>
){
    Scaffold(

    ) { innerPadding ->
        var addEditDialogVisible by remember { mutableStateOf(false) }
        var dialogItem by remember { mutableStateOf<Product?>(null) }
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

        LaunchedEffect(Unit){
            eventChannel.collect { event ->
                when(event){
                    InventoryEvents.NewProductSaved -> {
                        addEditDialogVisible = false
                    }
                    InventoryEvents.ProductUpdated -> {}
                    InventoryEvents.ProductDeleted -> {}
                    InventoryEvents.CategoryAdded -> {
                        uiAction(InventoryUiAction.ToggleCategoryDialog)
                    }
                    InventoryEvents.CategoryDeleted -> {}
                    InventoryEvents.CategoryUpdated -> {
                        uiAction(InventoryUiAction.ToggleCategoryDialog)
                    }
                    InventoryEvents.UnitAdded -> {
                        uiAction(InventoryUiAction.ToggleUnitDialog)
                    }
                    InventoryEvents.UnitDeleted -> {}
                    InventoryEvents.UnitUpdated -> {
                        uiAction(InventoryUiAction.ToggleUnitDialog)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
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
                        value = searchProductQuery,
                        onValueChange = { onSearchProductQueryChange(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InventoryTable(
                        uiAction = uiAction,
                        uiState = uiState
                    )
                }
                DeviceConfiguration.MOBILE_LANDSCAPE, DeviceConfiguration.TABLET_LANDSCAPE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SearchField(
                            value = uiState.searchProductQuery,
                            onValueChange = { uiAction(InventoryUiAction.OnSearchProductChange(it)) },
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
                    InventoryTable(
                        uiAction = uiAction,
                        uiState = uiState
                    )
                }
            }
        }
        if (addEditDialogVisible){
            AddEditInventoryDialog(
                item = dialogItem,
                onDismiss = { addEditDialogVisible = false },
                onSave = {
                    uiAction(InventoryUiAction.OnAddItemClick)
                },
                uiAction = uiAction,
                uiState = uiState
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
            InventoryScreen(
                uiState = InventoryUiState(),
                uiAction = {},
                searchProductQuery = "",
                onSearchProductQueryChange = {},
                eventChannel = emptyFlow()
            )
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun InventoryScreenPreviewDark(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            InventoryScreen(
                uiState = InventoryUiState(),
                uiAction = {},
                searchProductQuery = "",
                onSearchProductQueryChange = {},
                eventChannel = emptyFlow()
            )
        }
    )
}