package com.abdulmateen.pos_offline.feature.main.home.data.network.ktor
import com.abdulmateen.pos_offline.core.data.network.get
import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.feature.main.home.data.network.dto.ProductDto
import io.ktor.client.HttpClient

private const val BASE_URL = "https://fakestoreapi.com/"

class KtorProductsDataSource(
    private val httpClient: HttpClient
): RemoteProductsDataSource {
    override suspend fun fetchProducts(): Result<List<ProductDto>, DataError.Remote> {
        return httpClient.get(
                route = BASE_URL + "products"
            )
    }
}