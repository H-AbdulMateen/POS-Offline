package com.abdulmateen.cmpskeleton.feature.auth.data

import com.abdulmateen.cmpskeleton.core.domain.DataError
import com.abdulmateen.cmpskeleton.core.domain.Result
import com.abdulmateen.cmpskeleton.core.presentation.toUiText
import com.abdulmateen.cmpskeleton.feature.auth.data.network.FakeKtorLoginDataSource
import com.abdulmateen.cmpskeleton.feature.auth.data.network.FakeLoginResponse
import com.abdulmateen.cmpskeleton.feature.auth.data.network.FakeLoginResponseType
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRepositoryImplTest {

    @Test
    fun `test login success`() = runBlocking {
        val username = "test"
        val password = "password"
        val dataSource = FakeKtorLoginDataSource(responseType = FakeLoginResponseType.Success(FakeLoginResponse.success))
        val result = dataSource.login(username, password)
        println("$result")
        assertTrue(result is Result.Success)
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", result.data.token)
    }

    @Test
    fun `test login invalid credentials`() = runBlocking {
        val username = "test"
        val password = "password"
        val dataSource = FakeKtorLoginDataSource(
            responseType = FakeLoginResponseType.InvalidCredentials(FakeLoginResponse.error)
        )
        val result = dataSource.login(username, password)
        assertTrue(result is Result.Error)
        assertEquals(DataError.Remote.UNAUTHORIZED, result.error)
    }

    @Test
    fun `test login invalid login`() = runBlocking {
        val username = "test"
        val password = ""
        val dataSource = FakeKtorLoginDataSource(responseType = FakeLoginResponseType.InvalidRequest(FakeLoginResponse.invalidRequest))
        val result = dataSource.login(username, password)
        assertTrue(result is Result.Error)
        assertEquals(DataError.Remote.BAD_REQUEST, result.error)

    }


}