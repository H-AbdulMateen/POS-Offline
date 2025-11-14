package com.abdulmateen.pos_offline.feature.auth.domain

import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.Result
interface LoginRepository {
    suspend fun login(username: String, password: String): Result<String, DataError.Remote>
}