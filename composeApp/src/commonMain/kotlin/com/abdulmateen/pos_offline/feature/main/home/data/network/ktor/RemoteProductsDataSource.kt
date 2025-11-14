package com.abdulmateen.pos_offline.feature.main.home.data.network.ktor

import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.feature.main.home.data.network.dto.ProductDto
import com.abdulmateen.pos_offline.core.domain.Result

interface RemoteProductsDataSource {
    suspend fun fetchProducts(): Result<List<ProductDto>, DataError.Remote>
}