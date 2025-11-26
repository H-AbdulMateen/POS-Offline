package com.abdulmateen.pos_offline.feature.main.profile.presentation

sealed interface ProfileUiAction {
    data class UpdateBusinessName(val businessName: String) : ProfileUiAction
    data class UpdateBusinessAddress(val businessAddress: String) : ProfileUiAction
    data class UpdateBusinessPhone(val businessPhone: String) : ProfileUiAction
    data class UpdateBusinessEmail(val businessEmail: String) : ProfileUiAction
    data class UpdateSlogan(val slogan: String) : ProfileUiAction
    object SaveProfile : ProfileUiAction

}