package com.abdulmateen.pos_offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartupViewModel(
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private val _uiState = MutableStateFlow(LoadingUiState())
    private var hasLoadedInitialData: Boolean = false
    val uiState = _uiState
        .onStart {
            if (!hasLoadedInitialData) {
                observeSession()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoadingUiState()
        )

    init {
        viewModelScope.launch {
//            delay(1000L)
         observeSession()
        }
    }

    private suspend fun observeSession(){
        val authInfo = dataStoreManager.getBoolValue(PrefKeys.IS_LOGGED_IN)
        val setupCompleted = dataStoreManager.getBoolValue(PrefKeys.IS_SETUP_COMPLETED)
        _uiState.update {
            it.copy(
                isReady = true,
                isCheckingAuth = false,
                isLoggedIn = authInfo,
                isSetupCompleted = setupCompleted
            )
        }
    }
}