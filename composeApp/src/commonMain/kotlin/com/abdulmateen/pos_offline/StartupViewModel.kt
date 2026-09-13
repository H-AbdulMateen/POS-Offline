package com.abdulmateen.pos_offline

import co.touchlab.kermit.Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import com.abdulmateen.pos_offline.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartupViewModel(
    private val dataStoreManager: DataStoreManager,
    private val inventoryRepository: InventoryRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(LoadingUiState())
    private var hasLoadedInitialData: Boolean = false
    val uiState = _uiState
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
                viewModelScope.launch {
                    observeSession()
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoadingUiState()
        )

    private suspend fun observeSession(){
        Logger.d("StartupViewModel: Starting session observation...")
        try {
            Logger.d("StartupViewModel: Initializing inventory defaults...")
            inventoryRepository.initializeDefaults()
            
            Logger.d("StartupViewModel: Fetching auth info from DataStore...")
//            val authInfo = dataStoreManager.getBoolValue(PrefKeys.IS_LOGGED_IN)
            
            Logger.d("StartupViewModel: Fetching setup status...")
            val setupCompleted = dataStoreManager.getBoolValue(PrefKeys.IS_SETUP_COMPLETED)
            
//            Logger.d("StartupViewModel: Session observation complete. Auth: $authInfo, Setup: $setupCompleted")
            _uiState.update {
                it.copy(
                    isReady = true,
                    isCheckingAuth = false,
                    isLoggedIn = false,
                    isSetupCompleted = setupCompleted
                )
            }
        } catch (e: Exception) {
            Logger.e("StartupViewModel: Error during observeSession", e)
            _uiState.update { it.copy(isCheckingAuth = false) }
        }
    }
}
