package com.abdulmateen.pos_offline.feature.main.home.domain

import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.EmptyResult
import com.abdulmateen.pos_offline.core.domain.Result
import kotlinx.coroutines.flow.Flow


interface ProductRepository {
    suspend fun fetchProducts(): Result<List<Product>, DataError.Remote>
    fun loadAllProductsFromCache(): Flow<List<Product>>
    suspend fun markAsFavourite(productId: Int): EmptyResult<DataError.Local>
    fun getFavouriteProducts(): Flow<List<Product>>
    fun isProductFavourite(id: Int): Flow<Boolean>
    suspend fun deleteFromFavourite(id: Int)
    suspend fun removeFromFavourite(id: Int)

}