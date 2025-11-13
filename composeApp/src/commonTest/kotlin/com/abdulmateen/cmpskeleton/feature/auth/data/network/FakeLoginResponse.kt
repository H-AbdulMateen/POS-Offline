package com.abdulmateen.cmpskeleton.feature.auth.data.network

object FakeLoginResponse {
    val success = """
        {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        }
        """

    val error ="""username or password is incorrect"""
    val invalidRequest = """
            username and password are not provided in JSON format
        """.trimIndent()
}

sealed class FakeLoginResponseType {

    data class Success(val content: String) : FakeLoginResponseType()

    data class  InvalidCredentials(val content: String) : FakeLoginResponseType()

    data class InvalidRequest(val errorMessage: String) : FakeLoginResponseType()
}