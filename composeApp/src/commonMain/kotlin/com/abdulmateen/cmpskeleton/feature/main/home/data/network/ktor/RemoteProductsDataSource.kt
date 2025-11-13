package com.abdulmateen.cmpskeleton.feature.main.home.data.network.ktor

import com.abdulmateen.cmpskeleton.core.domain.DataError
import com.abdulmateen.cmpskeleton.feature.main.home.data.network.dto.ProductDto
import com.abdulmateen.cmpskeleton.core.domain.Result

interface RemoteProductsDataSource {
    suspend fun fetchProducts(): Result<List<ProductDto>, DataError.Remote>
}