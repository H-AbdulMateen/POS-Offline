package com.abdulmateen.pos_offline.feature.main.home.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.search

@Composable
fun ProductSearchSection(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit
) {
    OutlinedTF(
        value = value,
        onValueChange = onValueChange,
        placeholder = stringResource(Res.string.search),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = modifier
    )
}

@Preview(name = "Product Search Section")
@Composable
fun ProductSearchSectionPreview(){
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
fun ProductSearchSectionPreviewDark(){
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
