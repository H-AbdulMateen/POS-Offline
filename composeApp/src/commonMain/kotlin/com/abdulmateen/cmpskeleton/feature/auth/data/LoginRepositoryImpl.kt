package com.abdulmateen.cmpskeleton.feature.auth.data

import com.abdulmateen.cmpskeleton.core.domain.DataError
import com.abdulmateen.cmpskeleton.core.domain.Result
import com.abdulmateen.cmpskeleton.core.domain.map
import com.abdulmateen.cmpskeleton.feature.auth.data.network.RemoteUserDataSource
import com.abdulmateen.cmpskeleton.feature.auth.domain.LoginRepository

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