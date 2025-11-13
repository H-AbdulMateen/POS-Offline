package com.abdulmateen.cmpskeleton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.cmpskeleton.feature.auth.presentation.login.LoginScreen
import com.abdulmateen.cmpskeleton.feature.auth.presentation.login.LoginScreenRoot
import com.abdulmateen.cmpskeleton.navigation.AppNavGraph
import com.abdulmateen.cmpskeleton.ui.theme.CMPSkeletonTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    onAuthenticationChecked: () -> Unit = {}
) {
    val viewModel: StartupViewModel = koinViewModel()
    CMPSkeletonTheme {
        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
        LaunchedEffect(uiState.isCheckingAuth) {
            if(!uiState.isCheckingAuth) {
                onAuthenticationChecked()
            }
        }
        if (!uiState.isCheckingAuth) {
            AppNavGraph(
                isLoggedIn = uiState.isLoggedIn
            )
        }


//        if (getPlatform().os != "Android"  && !uiState.isReady) {
//            LoadingScreen()
//        }else {
//            AppNavGraph(
//                isLoggedIn = uiState.isLoggedIn
//            )
//        }
    }
}