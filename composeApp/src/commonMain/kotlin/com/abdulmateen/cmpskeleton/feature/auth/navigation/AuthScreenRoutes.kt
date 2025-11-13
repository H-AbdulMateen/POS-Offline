package com.abdulmateen.cmpskeleton.feature.auth.navigation

import kotlinx.serialization.Serializable

sealed interface AuthScreenRoutes {
    @Serializable
    data object Login: AuthScreenRoutes
    @Serializable
    data object ForgotPassword: AuthScreenRoutes
    @Serializable
    data object Otp: AuthScreenRoutes
    @Serializable
    data object SignUp: AuthScreenRoutes
}