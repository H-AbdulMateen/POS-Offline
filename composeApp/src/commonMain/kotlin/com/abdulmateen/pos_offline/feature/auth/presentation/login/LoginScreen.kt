package com.abdulmateen.pos_offline.feature.auth.presentation.login
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.login
import pos_offline.composeapp.generated.resources.password
import pos_offline.composeapp.generated.resources.sign_up
import pos_offline.composeapp.generated.resources.username
import com.abdulmateen.pos_offline.core.designsystem.components.dialogs.ErrorAlertDialog
import com.abdulmateen.pos_offline.core.designsystem.components.dialogs.LoadingDialog
import com.abdulmateen.pos_offline.core.designsystem.components.LogoImage
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTFPassword
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    navigateToSignUp: () -> Unit
){
    val viewModel: LoginViewModel = koinViewModel()
    LoginScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        uiAction = viewModel::uiAction,
        onLoginSuccess = onLoginSuccess,
        eventChannel = viewModel.eventChannel,
        navigateToSignUp = navigateToSignUp
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    uiAction: (LoginUiAction) -> Unit,
    eventChannel: Flow<LoginEvents>,
    onLoginSuccess: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        contentWindowInsets = WindowInsets.statusBars
    ) {innerPadding ->
        val rootModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .clip(RoundedCornerShape(
                topStart = 15.dp,
                topEnd = 15.dp
            ))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            )
            .consumeWindowInsets(WindowInsets.navigationBars)
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
        LaunchedEffect(Unit){
            eventChannel.collect {event ->
                when(event){
                    LoginEvents.OnSuccess -> {
                        onLoginSuccess()
                    }
                    LoginEvents.OnError -> {}
                }

            }
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ){
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                when(deviceConfiguration){
                    DeviceConfiguration.MOBILE_PORTRAIT -> {
                        LoginHeaderSection()
                        LoginFormSection(
                            uiState = uiState,
                            uiAction = uiAction,
                            navigateToSignUp = navigateToSignUp
                        )
                    }
                    DeviceConfiguration.MOBILE_LANDSCAPE -> {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LoginHeaderSection(
                                modifier = Modifier.fillMaxHeight()
                            )
                            LoginFormSection(
                                uiState = uiState,
                                uiAction = uiAction,
                                navigateToSignUp = navigateToSignUp
                            )

                        }
                    }
                    DeviceConfiguration.TABLET_PORTRAIT,
                    DeviceConfiguration.TABLET_LANDSCAPE,
                    DeviceConfiguration.DESKTOP -> {
                        LoginHeaderSection()
                        LoginFormSection(
                            uiState = uiState,
                            uiAction = uiAction,
                            navigateToSignUp = navigateToSignUp,
                            modifier = Modifier.widthIn(max = 540.dp)
                        )
                    }
                }



            }
            LoadingDialog(
                isVisible = uiState.isLoading,
                onDismissRequest = { }
            )

        }
        if (uiState.isAlertDialogOpened){
            ErrorAlertDialog(
                onDismissRequest = { uiAction(LoginUiAction.ToggleAlertDialog) },
                onConfirmation = { uiAction(LoginUiAction.ToggleAlertDialog) },
                dialogTitle = "Error",
                dialogText = uiState.errorMessage.asString()
            )
        }

    }
}

@Composable
fun LoginHeaderSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(resource = Res.string.login),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        LogoImage(modifier = Modifier.size(120.dp))
    }
}

@Composable
fun LoginFormSection(
    uiState: LoginUiState,
    uiAction: (LoginUiAction) -> Unit,
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTF(
                value = uiState.username,
                onValueChange = { uiAction(LoginUiAction.UpdateUsername(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(resource = Res.string.username)
            )
            OutlinedTFPassword(
                value = uiState.password,
                onValueChange = { uiAction(LoginUiAction.UpdatePassword(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(resource = Res.string.password),
                hasError = uiState.hasPasswordError,
                errorMessage = uiState.passwordErrorMessage
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
//                            onClick = { onLoginSuccess() }
                onClick = { uiAction(LoginUiAction.OnLoginClicked)},
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = stringResource(resource = Res.string.login))
            }
            Text(
                text = "OR"
            )
            Button(
                onClick = { navigateToSignUp() },
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = stringResource(Res.string.sign_up))
            }

        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        uiState = LoginUiState(),
        uiAction = {},
        onLoginSuccess = {},
        eventChannel = emptyFlow(),
        navigateToSignUp = {}
    )
}

