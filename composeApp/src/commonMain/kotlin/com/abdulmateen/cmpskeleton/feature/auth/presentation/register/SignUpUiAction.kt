package com.abdulmateen.cmpskeleton.feature.auth.presentation.register

import coil3.Bitmap
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

sealed interface SignUpUiAction {
    data class UpdateImageBitmap(val bitmap: Bitmap?): SignUpUiAction
    data class UpdateFullName(val text: String): SignUpUiAction
    data class UpdateUsername(val text: String): SignUpUiAction
    data class UpdateEmail(val text: String): SignUpUiAction
    data class UpdateDateOfBirth @OptIn(ExperimentalTime::class) constructor(val date: Instant): SignUpUiAction
    data class UpdatePassword(val text: String): SignUpUiAction
    data class UpdateConfirmPassword(val text: String): SignUpUiAction
    data object ToggleDatePickerDialogVisibility: SignUpUiAction
    data object SignUp: SignUpUiAction

}