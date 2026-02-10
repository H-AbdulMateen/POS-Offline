package com.abdulmateen.pos_offline.feature.inventory.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.add_item
import pos_offline.composeapp.generated.resources.inventory

@Composable
fun InventoryHeaderRow(
    onAddItemClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier =  modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(Res.string.inventory), style = MaterialTheme.typography.headlineSmall)
        Button(
            onClick = onAddItemClick,
            shape = MaterialTheme.shapes.small) {
            Text(text = stringResource(Res.string.add_item))
        }
    }
}

@Preview(name = "LightMode")
@Composable
private fun InventoryHeaderRowPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.InventoryHeaderRow(
                onAddItemClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
@Preview(name = "DarkMode")
@Composable
private fun InventoryHeaderRowPreviewDark(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.components.InventoryHeaderRow(
                onAddItemClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}