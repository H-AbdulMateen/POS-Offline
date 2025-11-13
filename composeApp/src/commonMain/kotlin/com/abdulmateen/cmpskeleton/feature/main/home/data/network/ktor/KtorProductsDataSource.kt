package com.abdulmateen.cmpskeleton.feature.main.home.data.network.ktor
import com.abdulmateen.cmpskeleton.core.data.network.get
import com.abdulmateen.cmpskeleton.core.domain.DataError
import com.abdulmateen.cmpskeleton.core.domain.Result
import com.abdulmateen.cmpskeleton.feature.main.home.data.network.dto.ProductDto
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