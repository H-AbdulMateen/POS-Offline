package com.abdulmateen.pos_offline.feature.auth.data

import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.core.domain.map
import com.abdulmateen.pos_offline.feature.auth.data.network.RemoteUserDataSource
import com.abdulmateen.pos_offline.feature.auth.domain.LoginRepository

class LoginRepositoryImpl(
    private val remoteUserDataSource: RemoteUserDataSource
): LoginRepository {
    override suspend fun login(
        username: String,
        password: String
    ): Result<String, DataError.Remote> {
        return remoteUserDataSource.login(username, password).map { it.token }
    }

}