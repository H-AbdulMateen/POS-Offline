package com.abdulmateen.pos_offline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.navigation.AppNavGraph
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    onAuthenticationChecked: () -> Unit = {}
) {
    val viewModel: StartupViewModel = koinViewModel()
    var isDarkTheme by rememberSaveable{ mutableStateOf(false) }
    POSOfflineTheme(
        darkTheme = isDarkTheme,
        content = {
            val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
            LaunchedEffect(uiState.isCheckingAuth) {
                if(!uiState.isCheckingAuth) {
                    onAuthenticationChecked()
                }
            }
            if (!uiState.isCheckingAuth) {
                AppNavGraph(
                    isLoggedIn = uiState.isLoggedIn,
                    isSetupCompleted = uiState.isSetupCompleted,
                    toggleDarkTheme = {
                        isDarkTheme = !isDarkTheme
                    },
                    isDarkTheme = isDarkTheme
                )
            }
        }
    )
}