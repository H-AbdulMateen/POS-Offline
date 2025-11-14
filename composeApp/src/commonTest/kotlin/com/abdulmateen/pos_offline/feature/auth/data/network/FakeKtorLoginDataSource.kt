package com.abdulmateen.pos_offline.feature.auth.data.network

import com.abdulmateen.pos_offline.core.data.network.safeCall
import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.feature.auth.data.network.dto.LoginResponseDto

class FakeKtorLoginDataSource(
    private val responseType: FakeLoginResponseType
) : RemoteUserDataSource {

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponseDto, DataError.Remote> {

        return safeCall<LoginResponseDto> {
            FakeLoginHttpFactory(
                type = responseType
            ).postRequest(
                body = """{
    "username": "$username",
    "password": "$password"
    }""".trimIndent()
            )
        }

    }
}