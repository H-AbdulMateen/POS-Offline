package com.abdulmateen.pos_offline.feature.setup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SetupUiState(
    val email: String = "",
    val brandName: String = "",
    val businessName: String = "",
    val phone: String = "",
    val currencySymbol: String = "$",
    val emailError: String? = null,
    val brandNameError: String? = null,
    val businessNameError: String? = null,
    val phoneError: String? = null,
    val isSetupCompleted: Boolean = false
)

class SetupViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}\$".toRegex()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun onBrandNameChange(value: String) {
        _uiState.update { it.copy(brandName = value, brandNameError = null) }
    }

    fun onBusinessNameChange(value: String) {
        _uiState.update { it.copy(businessName = value, businessNameError = null) }
    }

    fun onPhoneChange(value: String) {
        _uiState.update { it.copy(phone = value, phoneError = null) }
    }

    fun onCurrencySymbolChange(value: String) {
        _uiState.update { it.copy(currencySymbol = value) }
    }

    fun completeSetup() {
        val email = _uiState.value.email.trim()
        val brandName = _uiState.value.brandName.trim()
        val businessName = _uiState.value.businessName.trim()
        val phone = _uiState.value.phone.trim()

        var hasError = false

        if (!email.matches(emailRegex)) {
            _uiState.update { it.copy(emailError = "Please enter a valid Gmail address") }
            hasError = true
        }

        if (brandName.isEmpty()) {
            _uiState.update { it.copy(brandNameError = "Brand name is required") }
            hasError = true
        }

        if (businessName.isEmpty()) {
            _uiState.update { it.copy(businessNameError = "Business name is required") }
            hasError = true
        }

        if (phone.isEmpty()) {
            _uiState.update { it.copy(phoneError = "Phone number is required") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            dataStoreManager.setStringValue(PrefKeys.EMAIL, email)
            dataStoreManager.setStringValue(PrefKeys.BRAND_NAME, brandName)
            dataStoreManager.setStringValue(PrefKeys.BUSINESS_NAME, businessName)
            dataStoreManager.setStringValue(PrefKeys.PHONE, phone)
            dataStoreManager.setStringValue(PrefKeys.CURRENCY_SYMBOL, _uiState.value.currencySymbol)
            dataStoreManager.setBoolValue(PrefKeys.IS_SETUP_COMPLETED, true)
            _uiState.update { it.copy(isSetupCompleted = true) }
        }
    }
}
