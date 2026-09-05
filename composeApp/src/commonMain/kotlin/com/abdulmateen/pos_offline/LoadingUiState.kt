package com.abdulmateen.pos_offline

data class LoadingUiState(
    val isReady: Boolean = false,
    val isCheckingAuth: Boolean = true,
    val isLoggedIn: Boolean = false,
    val isSetupCompleted: Boolean = false,
)
