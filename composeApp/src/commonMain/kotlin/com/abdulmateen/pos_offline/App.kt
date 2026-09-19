package com.abdulmateen.pos_offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import co.touchlab.kermit.Logger
import com.abdulmateen.pos_offline.core.designsystem.components.LogoImage
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
            val uiState = viewModel.uiState.collectAsState().value
            Logger.d("App: uiState.isCheckingAuth = ${uiState.isCheckingAuth}")
            LaunchedEffect(uiState.isCheckingAuth) {
                if (!uiState.isCheckingAuth) {
                    Logger.d("App: Authentication check complete, calling onAuthenticationChecked()")
                    onAuthenticationChecked()
                }
            }
            if (uiState.isCheckingAuth) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LogoImage(modifier = Modifier.size(120.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 3.dp
                        )
                    }
                }
            } else {
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
