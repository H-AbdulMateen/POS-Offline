package com.abdulmateen.cmpskeleton.feature.auth.domain

import com.abdulmateen.cmpskeleton.core.domain.DataError
import com.abdulmateen.cmpskeleton.core.domain.Result
interface LoginRepository {
    suspend fun login(username: String, password: String): Result<String, DataError.Remote>
}