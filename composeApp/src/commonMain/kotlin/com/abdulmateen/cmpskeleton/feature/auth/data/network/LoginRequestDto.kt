package com.abdulmateen.cmpskeleton.feature.auth.data.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("password")
    val password: String,
    @SerialName("username")
    val username: String
)