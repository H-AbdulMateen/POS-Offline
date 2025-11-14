package com.abdulmateen.pos_offline.feature.auth.presentation.login
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import com.abdulmateen.pos_offline.core.domain.onError
import com.abdulmateen.pos_offline.core.domain.onSuccess
import com.abdulmateen.pos_offline.core.designsystem.toUiText
import com.abdulmateen.pos_offline.feature.auth.domain.LoginRepository
import com.abdulmateen.pos_offline.utils.Validator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    private val _eventChannel = Channel<LoginEvents>()
    val eventChannel = _eventChannel.receiveAsFlow()


    fun uiAction(action: LoginUiAction) {
        when(action){
            LoginUiAction.OnLoginClicked -> { validateField() }
            is LoginUiAction.UpdateLoadingStatus -> {
                _uiState.update {
                    it.copy(
                        isLoading = action.loading
                    )
                }
            }
            is LoginUiAction.UpdatePassword -> {
                _uiState.update {
                    it.copy(
                        password = action.text
                    )
                }
            }
            is LoginUiAction.UpdatePasswordErrorStatus -> {
                _uiState.update {
                    it.copy(
                        hasPasswordError = action.hasError,
                        passwordErrorMessage = action.errorMessage
                    )
                }
            }
            is LoginUiAction.UpdateUsername -> {
                _uiState.update {
                    it.copy(
                        username = action.text
                    )
                }
            }
            is LoginUiAction.UpdateUsernameErrorStatus -> {
                _uiState.update {
                    it.copy(
                        hasUsernameError = action.hasError,
                        usernameErrorMessage = action.errorMessage
                    )
                }
            }
            LoginUiAction.ToggleAlertDialog -> {
                _uiState.update {
                    it.copy(
                        isAlertDialogOpened = !it.isAlertDialogOpened
                    )
                }
            }
        }
    }

    private fun validateField() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val isUsernameValid = Validator.validateNonEmpty(_uiState.value.username)
            if (!isUsernameValid.isValid)
                return@launch

            val isPasswordValid = Validator.validateNonEmpty(_uiState.value.password)
            if (!isPasswordValid.isValid)
                return@launch

            login()
        }
    }

    private suspend fun login(){
        repository.login(
            username = uiState.value.username,
            password = uiState.value.password
        )
            .onSuccess {result ->
                dataStoreManager.setBoolValue(PrefKeys.IS_LOGGED_IN, true)
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
                _eventChannel.send(LoginEvents.OnSuccess)
            }
            .onError {error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toUiText(),
                        isAlertDialogOpened = true
                    )
                }
            }
    }
}