package com.abdulmateen.pos_offline.feature.main.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
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