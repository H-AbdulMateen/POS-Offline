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
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeSession()
        }
    }

    private suspend fun observeSession(){
        Logger.d("StartupViewModel: Starting session observation...")
        try {
            Logger.d("StartupViewModel: Initializing inventory defaults...")
            // Ensure this doesn't block forever
            inventoryRepository.initializeDefaults()
            
            Logger.d("StartupViewModel: Fetching setup status from DataStore...")
            val setupCompleted = dataStoreManager.getBoolValue(PrefKeys.IS_SETUP_COMPLETED)
            Logger.d("StartupViewModel: Setup status retrieved: $setupCompleted")
            
            _uiState.update {
                it.copy(
                    isReady = true,
                    isCheckingAuth = false,
                    isLoggedIn = false,
                    isSetupCompleted = setupCompleted
                )
            }
            Logger.d("StartupViewModel: UI State updated. isCheckingAuth = false")
        } catch (e: Throwable) {
            Logger.e("StartupViewModel: Fatal error during observeSession", e)
            _uiState.update { 
                it.copy(
                    isCheckingAuth = false,
                    isReady = true 
                ) 
            }
        }
    }
}
