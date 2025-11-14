package com.abdulmateen.pos_offline.feature.auth.data.network.ktor

import com.abdulmateen.pos_offline.core.data.network.post
import com.abdulmateen.pos_offline.core.data.network.safeCall
import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.feature.auth.data.network.dto.LoginResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.feature.auth.data.network.LoginRequestDto
import com.abdulmateen.pos_offline.feature.auth.data.network.RemoteUserDataSource

private const val BASE_URL = "https://fakestoreapi.com/"

class KtorUserDataSource(
    private val httpClient: HttpClient
) : RemoteUserDataSource {
    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponseDto, DataError.Remote> {
        return httpClient.post(
            route = BASE_URL + "auth/login",
            body = LoginRequestDto(
                username = username,
                password = password
            )
        )
    }
}