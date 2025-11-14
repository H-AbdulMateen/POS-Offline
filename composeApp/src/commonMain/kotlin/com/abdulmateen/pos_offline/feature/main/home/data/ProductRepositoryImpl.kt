package com.abdulmateen.pos_offline.feature.main.home.data

import androidx.sqlite.SQLiteException
import com.abdulmateen.pos_offline.core.domain.DataError
import com.abdulmateen.pos_offline.core.domain.EmptyResult
import com.abdulmateen.pos_offline.feature.main.home.domain.ProductRepository
import com.abdulmateen.pos_offline.feature.main.home.data.network.ktor.RemoteProductsDataSource
import com.abdulmateen.pos_offline.core.domain.Result
import com.abdulmateen.pos_offline.core.domain.asEmptyDataResult
import com.abdulmateen.pos_offline.core.domain.map
import com.abdulmateen.pos_offline.feature.main.home.data.database.ProductDao
import com.abdulmateen.pos_offline.feature.main.home.domain.Product
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toProduct
import com.abdulmateen.pos_offline.feature.main.home.data.mappers.toProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val remoteDataSource: RemoteProductsDataSource,
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun fetchProducts(): Result<List<Product>, DataError.Remote> {
         return remoteDataSource
            .fetchProducts()
            .map { productDtoList ->
                addNewAndClearOldProducts(productDtoList.map { it.toProduct() })
                productDtoList.map { it.toProduct() }
            }
    }

    override fun loadAllProductsFromCache(): Flow<List<Product>> =
        productDao
            .getAllProducts()
            .map { productEntityList ->
                productEntityList.map { it.toProduct() }
            }


    private suspend fun addNewAndClearOldProducts(list: List<Product>) {
        productDao.deleteAllProducts()
        list.map { product -> productDao.upsert(product.toProductEntity()) }
    }

    override suspend fun markAsFavourite(productId: Int): EmptyResult<DataError.Local> =
        try {
            productDao.markAsFavourite(id = productId)
            Result.Success(Unit)
        } catch (ex: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }


    override fun getFavouriteProducts(): Flow<List<Product>> =
        productDao
            .getFavouriteProducts()
            .map { productEntityList ->
                productEntityList.map { it.toProduct() }
            }

    override fun isProductFavourite(id: Int): Flow<Boolean> {
        return productDao.getAllProducts()
            .map { productEntityList ->
                productEntityList.any { it.id == id }
            }

    }

    override suspend fun removeFromFavourite(id: Int) {
        productDao.removeFromFavourite(id = id)
    }

    override suspend fun deleteFromFavourite(id: Int) {
        productDao.deleteProduct(id = id)
    }


}