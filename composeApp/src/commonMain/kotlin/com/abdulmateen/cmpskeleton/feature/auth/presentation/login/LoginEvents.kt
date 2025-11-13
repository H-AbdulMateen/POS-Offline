package com.abdulmateen.cmpskeleton.feature.auth.presentation.login


sealed class LoginEvents {
    data object OnSuccess: LoginEvents()
    data object OnError: LoginEvents()
}