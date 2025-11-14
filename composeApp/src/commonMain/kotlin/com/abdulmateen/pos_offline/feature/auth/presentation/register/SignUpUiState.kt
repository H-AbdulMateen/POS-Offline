package com.abdulmateen.pos_offline.feature.auth.presentation.register

import coil3.Bitmap

data class SignUpUiState(
    val isLoading: Boolean = false,
    val selectedImage: Bitmap? = null,
    val hasImageSelected: Boolean = false,
    val imageErrorMessage: String = "",
    val fullName: String = "",
    val hasFullNameError: Boolean = false,
    val fullNameErrorMessage: String = "",
    val username: String = "",
    val hasUsernameError: Boolean = false,
    val usernameErrorMessage: String = "",
    val email: String = "",
    val hasEmailError: Boolean = false,
    val emailErrorMessage: String = "",
    val dateOfBirth: String = "",
    val hasDateOfBirthError: Boolean = false,
    val dateOfBirthErrorMessage: String = "",
    val password: String = "",
    val hasPasswordError: Boolean = false,
    val passwordErrorMessage: String = "",
    val confirmPassword: String = "",
    val hasConfirmPasswordError: Boolean = false,
    val confirmPasswordErrorMessage: String = "",
    val datePickerDialogVisibility: Boolean = false
)
