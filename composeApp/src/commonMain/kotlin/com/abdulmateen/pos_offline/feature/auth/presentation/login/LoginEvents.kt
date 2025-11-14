package com.abdulmateen.pos_offline.feature.auth.presentation.login


sealed class LoginEvents {
    data object OnSuccess: LoginEvents()
    data object OnError: LoginEvents()
}