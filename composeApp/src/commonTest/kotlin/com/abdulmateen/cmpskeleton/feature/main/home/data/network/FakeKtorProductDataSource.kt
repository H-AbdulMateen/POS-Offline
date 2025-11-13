package com.abdulmateen.cmpskeleton.feature.main.home.data.network

import com.abdulmateen.cmpskeleton.datasource.remote.ktor.FakeDataSourceResponseType
import com.abdulmateen.cmpskeleton.feature.main.home.data.network.ktor.KtorProductsDataSource
import com.abdulmateen.cmpskeleton.feature.main.home.data.network.ktor.RemoteProductsDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FakeKtorProductDataSource {
    companion object Factory{
        fun build(
            type: FakeDataSourceResponseType,
            url: String
        ): RemoteProductsDataSource {
            val mockEngine = HttpClient(MockEngine.Companion) {
                expectSuccess = false
                install(ContentNegotiation) {
                    json(Json {
                        explicitNulls = false
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = true
                        encodeDefaults = true
                        classDiscriminator = "#class"
                    })
                }
                engine {
                    addHandler { request ->
                        when (request.url.fullPath) {
                            url -> {
                                when (type) {
                                    is FakeDataSourceResponseType.SuccessData -> {
                                        respond(
                                            content = type.content,
                                            status = HttpStatusCode.Companion.OK,
                                            headers = headersOf(
                                                HttpHeaders.ContentType,
                                                ContentType.Application.Json.toString()
                                            )
                                        )
                                    }

                                    is FakeDataSourceResponseType.Empty -> {
                                        respond(
                                            content = type.body,
                                            status = HttpStatusCode.Companion.OK,
                                            headers = headersOf(
                                                HttpHeaders.ContentType,
                                                ContentType.Application.Json.toString()
                                            )
                                        )
                                    }

                                    is FakeDataSourceResponseType.Error -> {
                                        respond(
                                            content = type.errorMessage,
                                            status = HttpStatusCode.Companion.InternalServerError,
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
            return KtorProductsDataSource(mockEngine)
        }
    }
}