package com.abdulmateen.pos_offline.feature.profile.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.CenteredTopBarTitle
import com.abdulmateen.pos_offline.feature.profile.presentation.components.ProfileInputSection
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenRoot() {
    val viewModel = koinViewModel<ProfileViewModel>()
    ProfileScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        uiAction = viewModel::uiAction
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    uiAction: (ProfileUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            CenteredTopBarTitle(
                title = stringResource(Res.string.profile)
            )
        }
    ) {innerPadding ->
        ProfileInputSection(
            uiState = uiState,
            uiAction = uiAction,
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Preview(name = "LightMode")
@Composable
fun ProfileScreenPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            ProfileScreen(uiState = ProfileUiState(), uiAction = {})
        }
    )
}
@Preview(name = "DarkMode")
@Composable
fun ProfileScreenPreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            ProfileScreen(uiState = ProfileUiState(), uiAction = {})
        }
    )
}