package com.abdulmateen.pos_offline.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import com.abdulmateen.pos_offline.core.domain.PrefKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val email: String = "",
    val brandName: String = "",
    val businessName: String = "",
    val phone: String = "",
    val currencySymbol: String = "$",
    val isCustomizablePriceEnabled: Boolean = false,
    val isSaved: Boolean = false
)

class SettingsViewModel(
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    email = dataStoreManager.getStringValue(PrefKeys.EMAIL),
                    brandName = dataStoreManager.getStringValue(PrefKeys.BRAND_NAME),
                    businessName = dataStoreManager.getStringValue(PrefKeys.BUSINESS_NAME),
                    phone = dataStoreManager.getStringValue(PrefKeys.PHONE),
                    currencySymbol = dataStoreManager.getStringValue(PrefKeys.CURRENCY_SYMBOL).ifEmpty { "$" },
                    isCustomizablePriceEnabled = dataStoreManager.getBoolValue(PrefKeys.CUSTOMIZABLE_PRICE)
                )
            }
        }
    }

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, isSaved = false) }
    fun onBrandNameChange(value: String) = _uiState.update { it.copy(brandName = value, isSaved = false) }
    fun onBusinessNameChange(value: String) = _uiState.update { it.copy(businessName = value, isSaved = false) }
    fun onPhoneChange(value: String) = _uiState.update { it.copy(phone = value, isSaved = false) }
    fun onCurrencySymbolChange(value: String) = _uiState.update { it.copy(currencySymbol = value, isSaved = false) }
    fun onCustomizablePriceToggle(value: Boolean) = _uiState.update { it.copy(isCustomizablePriceEnabled = value, isSaved = false) }

    fun saveSettings() {
        viewModelScope.launch {
            dataStoreManager.setStringValue(PrefKeys.EMAIL, _uiState.value.email)
            dataStoreManager.setStringValue(PrefKeys.BRAND_NAME, _uiState.value.brandName)
            dataStoreManager.setStringValue(PrefKeys.BUSINESS_NAME, _uiState.value.businessName)
            dataStoreManager.setStringValue(PrefKeys.PHONE, _uiState.value.phone)
            dataStoreManager.setStringValue(PrefKeys.CURRENCY_SYMBOL, _uiState.value.currencySymbol)
            dataStoreManager.setBoolValue(PrefKeys.CUSTOMIZABLE_PRICE, _uiState.value.isCustomizablePriceEnabled)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun doLogoutUser(){
        viewModelScope.launch {
            dataStoreManager.setBoolValue(PrefKeys.IS_LOGGED_IN, false)
        }
    }
}
