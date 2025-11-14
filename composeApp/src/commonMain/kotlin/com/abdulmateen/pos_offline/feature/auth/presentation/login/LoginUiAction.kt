package com.abdulmateen.pos_offline.feature.auth.presentation.login

sealed class LoginUiAction {
    data class UpdateLoadingStatus(val loading: Boolean): LoginUiAction()
    data class UpdateUsername(val text: String): LoginUiAction()
    data class UpdateUsernameErrorStatus(val hasError: Boolean, val errorMessage: String): LoginUiAction()
    data class UpdatePassword(val text: String): LoginUiAction()
    data class UpdatePasswordErrorStatus(val hasError: Boolean, val errorMessage: String): LoginUiAction()
    data object ToggleAlertDialog: LoginUiAction()
    data object OnLoginClicked: LoginUiAction()
}