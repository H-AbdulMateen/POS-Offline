package com.abdulmateen.pos_offline.datasource.cache.dao

import com.abdulmateen.pos_offline.datasource.cache.FakeDatabase
import com.abdulmateen.pos_offline.feature.main.home.data.database.dao.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductDaoFake(
    private val db: FakeDatabase
): ProductDao {
    override suspend fun upsert(product: ProductEntity) {
        // 1. Get the current list from the flow's value.
        val currentList = db.products.value

        // 2. Find the index of the product with the same ID.
        val existingIndex = currentList.indexOfFirst { it.id == product.id }

        // 3. Create a new list based on whether the product exists.
        val newList = if (existingIndex != -1) {
            // It exists: create a new list with the product updated at that index.
            currentList.toMutableList().apply {
                this[existingIndex] = product
            }
        } else {
            // It's new: create a new list by adding the new product.
            currentList + product
        }

        // 4. Assign the new list to the StateFlow to emit the update.
        db.products.value = newList
    }

    override fun getAllProducts(): Flow<List<ProductEntity>> {
        return db.products
    }

    override suspend fun getProduct(id: Int): ProductEntity? {
        return db.products.value.find { it.id == id }
    }

    override fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return db.products.map { products ->
            if (query.isBlank()){
                products
            }else {
                products.filter { it.title.contains(query, ignoreCase = true) }
            }
        }
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return db.products.map { products ->
            products.find { it.id == id }?.isFavourite == false
        }
    }

    override fun getFavouriteProducts(): Flow<List<ProductEntity>> {
        return db.products.map { products ->
            products.filter { it.isFavourite }
        }
    }

    override suspend fun markAsFavourite(id: Int) {
        db.products.value.find { it.id == id }?.let { product ->
            upsert(product.copy(isFavourite = true))
        }
    }

    override suspend fun removeFromFavourite(id: Int) {
        getProduct(id)?.let { product ->
            upsert(product.copy(isFavourite = false))
        }
    }

    override suspend fun deleteProduct(id: Int) {
        db.products.value = db.products.value.filterNot { it.id == id }
    }

    override suspend fun deleteAllProducts() {
        db.products.value = emptyList()
    }
}