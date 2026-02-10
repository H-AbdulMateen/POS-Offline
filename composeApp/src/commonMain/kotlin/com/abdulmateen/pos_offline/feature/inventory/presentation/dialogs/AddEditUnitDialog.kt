package com.abdulmateen.pos_offline.feature.inventory.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.abdulmateen.pos_offline.core.designsystem.components.AnimatedErrorText
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.core.designsystem.components.TitleLargeText
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction
import com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.add_unit
import pos_offline.composeapp.generated.resources.cancel
import pos_offline.composeapp.generated.resources.edit_unit
import pos_offline.composeapp.generated.resources.save
import pos_offline.composeapp.generated.resources.unit_name
import pos_offline.composeapp.generated.resources.unit_symbol_name


@Composable
fun AddEditItemUnitDialog(
    itemUnit: ItemUnit?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    uiState: com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState,
    uiAction: (com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction) -> Unit
) {
    val isEditing = itemUnit != null

    Dialog(
        onDismissRequest = onDismiss
    ){
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
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isEditing){
                    TitleLargeText(title = stringResource(Res.string.edit_unit))
                }else{
                    TitleLargeText(title = stringResource(Res.string.add_unit))
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTF(
                    value = uiState.itemUnitName,
                    onValueChange = { uiAction(_root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.OnUnitNameChange(it)) },
                    placeholder = stringResource(Res.string.unit_name),
                    hasError = uiState.hasItemUnitNameError,
                    errorMessage = uiState.itemUnitNameErrorText
                )
                Spacer(modifier = Modifier.height(2.dp))
                OutlinedTF(
                    value = uiState.itemUnitSymbol,
                    onValueChange = { uiAction(_root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiAction.OnUnitSymbolChange(it)) },
                    placeholder = stringResource(Res.string.unit_symbol_name),
                    hasError = uiState.hasItemUnitSymbolError,
                    errorMessage = uiState.itemUnitSymbolErrorText
                )

                AnimatedErrorText(
                    modifier = Modifier.fillMaxWidth(),
                    visible = uiState.unitErrorResult != null,
                    errorMessage = uiState.unitErrorResult?.asString()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Button(
                        onClick = onDismiss,
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = stringResource(Res.string.cancel))

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        shape = MaterialTheme.shapes.small
                    ){
                        Text(text = stringResource(Res.string.save))
                    }
                }

            }
        }
    }
}

@Preview(name = "LightMode")
@Composable
fun ItemUnitDialogPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.dialogs.AddEditItemUnitDialog(
                onDismiss = {},
                onConfirm = {},
                itemUnit = null,
                uiState = _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState(),
                uiAction = {}
            )
        }
    )
}
@Preview(name = "DarkMode")
@Composable
fun ItemUnitDialogDarkPreview(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.dialogs.AddEditItemUnitDialog(
                onDismiss = {},
                onConfirm = {},
                itemUnit = null,
                uiState = _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryUiState(),
                uiAction = {}
            )
        }
    )
}