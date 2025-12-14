package com.abdulmateen.pos_offline.feature.main.home.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abdulmateen.pos_offline.core.designsystem.components.CenteredTopBarNavTitle
import com.abdulmateen.pos_offline.feature.main.home.presentation.components.CartSummarySection
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.cart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit = {}
){
    Scaffold(
        topBar = {
            CenteredTopBarNavTitle(
                title = stringResource(Res.string.cart),
                onBackClick = onBackClick
            )
        }
    ) {innerPadding ->
        CartSummarySection(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(name = "Light Mode")
@Composable
fun CartScreenPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            CartScreen()
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun CartScreenDarkPreview(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            CartScreen()
        }
    )
}