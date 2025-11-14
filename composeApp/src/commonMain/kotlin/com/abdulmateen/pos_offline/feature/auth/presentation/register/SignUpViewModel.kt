package com.abdulmateen.pos_offline.feature.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.utils.formatDatePlatform
import com.abdulmateen.pos_offline.utils.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class SignUpViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalTime::class)
    fun uiAction(action: SignUpUiAction) {
        when (action) {
            is SignUpUiAction.UpdateImageBitmap -> {
                _uiState.update {
                    it.copy(
                        selectedImage = action.bitmap
                    )
                }
            }

            is SignUpUiAction.UpdateConfirmPassword -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = action.text
                    )
                }
            }

            is SignUpUiAction.UpdateDateOfBirth -> {
                val formattedDate = formatDatePlatform(date = action.date, outputPattern = "dd/MM/yyyy")
                _uiState.update {
                    it.copy(
                        dateOfBirth = formattedDate
                    )
                }
            }

            is SignUpUiAction.UpdateEmail -> {
                _uiState.update {
                    it.copy(
                        email = action.text
                    )
                }
            }

            is SignUpUiAction.UpdateFullName -> {
                _uiState.update {
                    it.copy(
                        fullName = action.text
                    )
                }
            }

            is SignUpUiAction.UpdatePassword -> {
                _uiState.update {
                    it.copy(
                        password = action.text
                    )
                }
            }

            is SignUpUiAction.UpdateUsername -> {
                _uiState.update {
                    it.copy(
                        username = action.text
                    )
                }
            }

            SignUpUiAction.ToggleDatePickerDialogVisibility -> {
                _uiState.update {
                    it.copy(
                        datePickerDialogVisibility = !it.datePickerDialogVisibility
                    )
                }
            }

            SignUpUiAction.SignUp -> {
                validateFields()
            }
        }
    }

    private fun validateFields() {
        viewModelScope.launch {
            if (uiState.value.selectedImage == null){
                updateImageErrorStatus(
                    hasError = true,
                    errorMessage = "Please select an image"
                )
                return@launch
            }
            val validateFullName = Validator.validateNonEmpty(uiState.value.fullName)
            if (!validateFullName.isValid){
                updateFullNameErrorStatus(
                    hasError = true,
                    errorMessage = validateFullName.errorMessage
                )
                return@launch
            }
            val validateUsername = Validator.validateNonEmpty(uiState.value.username)
            if (!validateUsername.isValid){
                updateUsernameErrorStatus(
                    hasError = true,
                    errorMessage = validateUsername.errorMessage
                )
                return@launch
            }
            val validateEmail = Validator.validateEmail(uiState.value.email)
            if (!validateEmail.isValid){
                updateEmailErrorStatus(
                    hasError = true,
                    errorMessage = validateEmail.errorMessage
                )
                return@launch
            }

            signUp()

        }

    }

    private suspend fun signUp(){

    }

    private fun updateImageErrorStatus(
        hasError: Boolean,
        errorMessage: String
    ) {
        _uiState.update {
            it.copy(
                hasImageSelected = hasError,
                imageErrorMessage = errorMessage
            )
        }
    }

    private fun updateUsernameErrorStatus(
        hasError: Boolean,
        errorMessage: String
    ) {
        _uiState.update {
            it.copy(
                hasUsernameError = hasError,
                usernameErrorMessage = errorMessage
            )
        }
    }

    private fun updateFullNameErrorStatus(
        hasError: Boolean,
        errorMessage: String
    ) {
        _uiState.update {
            it.copy(
                hasFullNameError = hasError,
                fullNameErrorMessage = errorMessage
            )
        }
    }

    private fun updateEmailErrorStatus(
        hasError: Boolean,
        errorMessage: String
    ) {
        _uiState.update {
            it.copy(
                hasEmailError = hasError,
                emailErrorMessage = errorMessage
            )
        }
    }

    private fun updateDateOfBirthErrorStatus(
        hasError: Boolean,
        errorMessage: String
    ) {
        _uiState.update {
            it.copy(
                hasDateOfBirthError = hasError,
                dateOfBirthErrorMessage = errorMessage
            )
        }
    }


    private fun updatePasswordErrorStatus(
        hasError: Boolean,
        errorMessage: String
    ) {
        _uiState.update {
            it.copy(
                hasPasswordError = hasError,
                passwordErrorMessage = errorMessage
            )
        }
    }

    private fun updateConfirmPasswordErrorStatus(
        hasError: Boolean,
        errorMessage: String
    ) {
        _uiState.update {
            it.copy(
                hasConfirmPasswordError = hasError,
                confirmPasswordErrorMessage = errorMessage
            )
        }
    }


}