package com.abdulmateen.cmpskeleton.feature.main.home.data.repository

import com.abdulmateen.cmpskeleton.core.domain.Result
import com.abdulmateen.cmpskeleton.core.domain.map
import com.abdulmateen.cmpskeleton.datasource.cache.FakeDatabase
import com.abdulmateen.cmpskeleton.datasource.cache.dao.ProductDaoFake
import com.abdulmateen.cmpskeleton.feature.main.home.data.mappers.toProduct
import com.abdulmateen.cmpskeleton.feature.main.home.data.network.FakeKtorProductDataSource
import com.abdulmateen.cmpskeleton.feature.main.home.data.network.FakeProductDataGenerator
import com.abdulmateen.cmpskeleton.datasource.remote.ktor.FakeDataSourceResponseType
import com.abdulmateen.cmpskeleton.feature.main.home.data.mappers.toProductEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductRepositoryImplTest {
    private val db = FakeDatabase()
    private val productUrl = "/products"
    private lateinit var productDaoFake: ProductDaoFake

    @BeforeTest
    fun setup() {
        productDaoFake = ProductDaoFake(db = db)
    }

    @Test
    fun `test fetchProducts success`() = runBlocking {
        val dataSource = FakeKtorProductDataSource.build(type = FakeDataSourceResponseType.SuccessData(content = FakeProductDataGenerator.jsonData), url = productUrl)
        val result = dataSource.fetchProducts()
        productDaoFake.deleteAllProducts()
        assertEquals(0, productDaoFake.getAllProducts().first().size)
        result.map { productDtoList -> productDtoList.map {
            productDto -> productDaoFake.upsert(productDto.toProduct().toProductEntity())
        } }
        assertTrue(productDaoFake.getAllProducts().first().isNotEmpty())
        assertTrue(actual = result is Result.Success)
        assertEquals(FakeProductDataGenerator.productList.map { productDto -> productDto.toProduct() }, actual =  result.data.map { productDto ->  productDto.toProduct() })
    }

    @Test
    fun `test fetchProducts emptyList`() = runBlocking {
        val dataSource = FakeKtorProductDataSource.build(type = FakeDataSourceResponseType.Empty(body = FakeProductDataGenerator.empty), url = productUrl)
        val result = dataSource.fetchProducts()

        assertTrue(actual = result is Result.Success)
    }
    @Test
    fun `test fetchProducts error`() = runBlocking {
        val dataSource = FakeKtorProductDataSource.build(FakeDataSourceResponseType.Error(errorMessage = FakeProductDataGenerator.error), url = productUrl)
        val result = dataSource.fetchProducts()
        assertTrue(actual = result is Result.Error)
    }
}