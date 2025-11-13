package com.abdulmateen.cmpskeleton.feature.auth.data.network

import com.abdulmateen.cmpskeleton.core.domain.DataError
import com.abdulmateen.cmpskeleton.feature.auth.data.network.dto.LoginResponseDto
import com.abdulmateen.cmpskeleton.core.domain.Result

interface RemoteUserDataSource {
    suspend fun login(username: String, password: String): Result<LoginResponseDto, DataError.Remote>
}