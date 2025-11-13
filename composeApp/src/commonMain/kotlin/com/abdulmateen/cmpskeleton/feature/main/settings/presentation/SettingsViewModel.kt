package com.abdulmateen.cmpskeleton.feature.main.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.cmpskeleton.core.domain.DataStoreManager
import com.abdulmateen.cmpskeleton.core.domain.PrefKeys
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    fun doLogoutUser(){
        viewModelScope.launch {
            dataStoreManager
                .setBoolValue(
                    key = PrefKeys.IS_LOGGED_IN,
                    value = false
                )
        }
    }
}