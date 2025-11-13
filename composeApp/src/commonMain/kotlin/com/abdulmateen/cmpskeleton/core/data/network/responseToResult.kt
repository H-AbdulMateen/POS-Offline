package com.abdulmateen.cmpskeleton.core.data.network

import co.touchlab.kermit.Logger
import com.abdulmateen.cmpskeleton.core.domain.DataError
import com.abdulmateen.cmpskeleton.core.domain.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.Remote> {
    val log = Logger.withTag("KtorResponseToResult")
    return when(response.status.value) {
        in 200..299 -> {
            try {
                log.i { "Response Body: ${response.body<Any>()}" }
                Result.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                log.e { "Response Body: ${response.body<String>()} , Exception: ${e.message}" }
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        400 -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.BAD_REQUEST)
        }
        401 -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.UNAUTHORIZED)
        }
        403 -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.FORBIDDEN)
        }
        408 -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        }
        409 -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.CONFLICT)
        }
        413 -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.PAYLOAD_TOO_LARGE)
        }
        429 -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        }
        in 500..599 -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.SERVER)
        }

        else -> {
            log.e { "Response Body: ${response.body<String>()}" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}