package com.abdulmateen.pos_offline.feature.auth.presentation.login

import com.abdulmateen.pos_offline.core.designsystem.UiText

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