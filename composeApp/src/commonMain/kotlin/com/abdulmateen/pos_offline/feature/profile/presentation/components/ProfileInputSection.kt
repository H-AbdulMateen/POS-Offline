package com.abdulmateen.pos_offline.feature.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedPhoneTF
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.feature.profile.presentation.ProfileUiAction
import com.abdulmateen.pos_offline.feature.profile.presentation.ProfileUiState
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.business_address
import pos_offline.composeapp.generated.resources.business_name
import pos_offline.composeapp.generated.resources.enter_your_business_detail
import pos_offline.composeapp.generated.resources.phone

@Composable
fun ProfileInputSection(
    uiState: ProfileUiState,
    uiAction: (ProfileUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(Res.string.enter_your_business_detail))
        OutlinedTF(
            value = uiState.businessName,
            onValueChange = { uiAction(ProfileUiAction.UpdateBusinessName(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(Res.string.business_name)
        )

        OutlinedPhoneTF(
            phoneNumber = uiState.businessPhone,
            onPhoneNumberChange = { uiAction(ProfileUiAction.UpdateBusinessPhone(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(Res.string.phone)
        )

        OutlinedTF(
            value = uiState.businessAddress,
            onValueChange = { uiAction(ProfileUiAction.UpdateBusinessAddress(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(Res.string.business_address)
        )
    }
}

@Preview(name = "LightMode", showBackground = true)
@Composable
fun ProfileInputSectionPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            ProfileInputSection(
                uiState = ProfileUiState(),
                uiAction = {}
            )
        }
    )
}
@Preview(name = "DarkMode")
@Composable
fun ProfileInputSectionPreviewDark(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            ProfileInputSection(
                uiState = ProfileUiState(),
                uiAction = {}
            )
        }
    )
}