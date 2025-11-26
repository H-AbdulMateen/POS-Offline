package com.abdulmateen.pos_offline.feature.main.profile.presentation

data class ProfileUiState(
    val isLoading: Boolean = false,
    val businessName: String = "",
    val businessAddress: String = "",
    val businessPhone: String = "",
    val businessEmail: String = "",
    val slogan: String = ""
)
