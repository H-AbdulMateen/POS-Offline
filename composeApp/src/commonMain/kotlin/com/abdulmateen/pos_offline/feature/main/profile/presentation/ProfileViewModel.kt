package com.abdulmateen.pos_offline.feature.main.profile.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ProfileViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun uiAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.UpdateBusinessAddress -> {}
            is ProfileUiAction.UpdateBusinessEmail -> {}
            is ProfileUiAction.UpdateBusinessName -> {}
            is ProfileUiAction.UpdateBusinessPhone -> {}
            is ProfileUiAction.UpdateSlogan -> {}
            ProfileUiAction.SaveProfile -> {}
        }
    }



}