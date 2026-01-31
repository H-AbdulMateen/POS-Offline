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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.SearchField
import com.abdulmateen.pos_offline.core.designsystem.components.layouts.MySnackBarScaffold
import com.abdulmateen.pos_offline.core.presentation.util.ObserveAsEvents
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.InventoryHeaderRow
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.InventoryTable
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.dialogs.AddEditInventoryDialog
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.dialogs.ProductDetailDialog
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.getString
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
    val snackBarState = remember { SnackbarHostState() }

    InventoryScreen(
        uiState = uiState,
        uiAction = viewModel::uiAction,
        onSearchProductQueryChange = viewModel::onSearchProductQueryChange,
        searchProductQuery = searchProductQuery,
        snackbarHostState = snackBarState
    )

    ObserveAsEvents(viewModel.eventChannel){event ->
        when(event){
            is InventoryEvents.OnSuccess -> {
                snackBarState.showSnackbar(message = getString(event.message))
            }
            is InventoryEvents.OnError -> {
                snackBarState.showSnackbar(message = getString(event.message))
            }
        }
    }

}


@Composable
fun InventoryScreen(
    searchProductQuery: String,
    uiState: InventoryUiState = InventoryUiState(),
    onSearchProductQueryChange: (String) -> Unit,
    uiAction: (InventoryUiAction) -> Unit,
    snackbarHostState: SnackbarHostState
){
    MySnackBarScaffold(
        snackbarHostState = snackbarHostState
    ) {
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
                            uiAction(InventoryUiAction.ToggleAddEditProductDialog(item = null))
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
                                uiAction(InventoryUiAction.ToggleAddEditProductDialog(item = null))
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
        if (uiState.addEditProductDialog){
            AddEditInventoryDialog(
                item = uiState.selectedProduct,
                onDismiss = { uiAction(InventoryUiAction.ToggleAddEditProductDialog(item = null)) },
                onSave = {
                    uiAction(InventoryUiAction.OnAddItemClick)
                },
                uiAction = uiAction,
                uiState = uiState
            )
        }

        if (uiState.detailProductDialog){
            uiState.selectedProduct?.let {
                ProductDetailDialog(
                    product = it,
                    onDismiss = { uiAction(InventoryUiAction.ToggleDetailDialog(item = null)) },
                )
            }
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
                snackbarHostState = remember { SnackbarHostState() }
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
                snackbarHostState = remember { SnackbarHostState() }
            )
        }
    )
}