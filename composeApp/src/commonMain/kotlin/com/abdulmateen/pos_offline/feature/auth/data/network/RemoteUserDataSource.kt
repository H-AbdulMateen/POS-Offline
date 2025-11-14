package com.abdulmateen.pos_offline.feature.auth.data.network

import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.feature.auth.data.network.dto.LoginResponseDto
import com.abdulmateen.pos_offline.core.domain.Result

interface RemoteUserDataSource {
    suspend fun login(username: String, password: String): Result<LoginResponseDto, DataError.Remote>
}