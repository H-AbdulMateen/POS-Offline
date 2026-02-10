package com.abdulmateen.pos_offline.feature.home.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.CenteredTopBarNavTitle
import com.abdulmateen.pos_offline.feature.home.presentation.components.CartSummarySection
import com.abdulmateen.pos_offline.feature.home.presentation.order.OrderUiAction
import com.abdulmateen.pos_offline.feature.home.presentation.order.OrderUiState
import com.abdulmateen.pos_offline.feature.home.presentation.order.OrderViewModel
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.cart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenRoot(
    onBackClick: () -> Unit = {}
){
    val orderViewModel: OrderViewModel = koinViewModel()
    CartScreen(
        onBackClick = onBackClick,
        uiState = orderViewModel.uiState.collectAsStateWithLifecycle().value,
        uiAction = orderViewModel::uiAction
    )
}

@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    uiState: OrderUiState,
    uiAction: (OrderUiAction) -> Unit
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
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            uiAction = uiAction
        )
    }
}

@Preview(name = "Light Mode")
@Composable
fun CartScreenPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            CartScreen(
                uiState = OrderUiState(),
                uiAction = {}
            )
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun CartScreenDarkPreview(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            CartScreen(
                uiState = OrderUiState(),
                uiAction = {}
            )
        }
    )
}