package com.abdulmateen.cmpskeleton

data class LoadingUiState(
    val isReady: Boolean = false,
    val isCheckingAuth: Boolean = true,
    val isLoggedIn: Boolean = false,
)
