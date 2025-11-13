package com.abdulmateen.cmpskeleton.feature.auth.presentation.login

import cmpskeleton.composeapp.generated.resources.Res
import cmpskeleton.composeapp.generated.resources.error_unknown
import com.abdulmateen.cmpskeleton.core.presentation.UiText
import org.jetbrains.compose.resources.StringResource

data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String = "johnd",
    val hasUsernameError: Boolean = false,
    val usernameErrorMessage: String = "",
    val password: String = "m38rmF$",
    val hasPasswordError: Boolean = false,
    val passwordErrorMessage: String = "",
    val errorMessage: UiText = UiText.DynamicString(""),
    val isAlertDialogOpened: Boolean = false,
    val navigateToMain: Boolean = false
)