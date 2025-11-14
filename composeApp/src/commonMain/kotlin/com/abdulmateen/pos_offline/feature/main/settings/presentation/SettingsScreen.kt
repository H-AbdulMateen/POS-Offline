package com.abdulmateen.pos_offline.feature.main.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.logout
import pos_offline.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreenRoot(
    onLogoutClick: () -> Unit
){
    val viewModel: SettingsViewModel = koinViewModel()
    SettingsScreen(
        onLogoutClick = onLogoutClick,
        doLogoutUser = viewModel::doLogoutUser
    )
}

@Composable
fun SettingsScreen(
    onLogoutClick: () -> Unit,
    doLogoutUser: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(text = stringResource(Res.string.settings))

            Button(
                onClick = {
                    doLogoutUser()
                    onLogoutClick()
                }
            ){
                Text(text = stringResource(Res.string.logout))
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        onLogoutClick = {},
        doLogoutUser = {}
    )
}