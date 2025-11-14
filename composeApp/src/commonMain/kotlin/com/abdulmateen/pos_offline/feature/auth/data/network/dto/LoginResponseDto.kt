package com.abdulmateen.pos_offline.feature.auth.data.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("token")
    val token: String
)