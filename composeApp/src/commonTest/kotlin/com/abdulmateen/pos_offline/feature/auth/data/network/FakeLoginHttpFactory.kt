package com.abdulmateen.pos_offline.feature.auth.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FakeLoginHttpFactory(
    val type: FakeLoginResponseType
) {
    val url = "/auth/login"
    val httpClient = HttpClient(MockEngine.Companion) {
        expectSuccess = false
        install(ContentNegotiation) {
            json(
                Json {
                    explicitNulls = false
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    encodeDefaults = true
                    classDiscriminator = "#class"
                }
            )
        }
        engine {
            addHandler { request ->
                when (request.url.fullPath) {
                    url -> {
                        when (type) {
                            is FakeLoginResponseType.Success -> {
                                respond(
                                    content = type.content,
                                    status = HttpStatusCode.Companion.OK,
                                    headers = headersOf(
                                        HttpHeaders.ContentType,
                                        ContentType.Application.Json.toString()
                                    )
                                )
                            }

                            is FakeLoginResponseType.InvalidCredentials -> {
                                respond(
                                    content = type.content,
                                    status = HttpStatusCode.Companion.Unauthorized,
                                    headers = headersOf(
                                        HttpHeaders.ContentType,
                                        ContentType.Text.Html.toString()
                                    )
                                )
                            }

                            is FakeLoginResponseType.InvalidRequest -> {
                                respond(
                                    content = type.errorMessage,
                                    status = HttpStatusCode.Companion.BadRequest,
                                    headers = headersOf(
                                        HttpHeaders.ContentType,
                                        ContentType.Application.Json.toString()
                                    )
                                )
                            }
                        }

                    }

                    else -> error("Unhandled request: ${request.url.fullPath}")
                }
            }
        }
    }

    suspend inline fun <reified T> postRequest(
        body: T
    ): HttpResponse =
        httpClient.post(
            urlString = url,
        ) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

}