package com.abdulmateen.cmpskeleton.datasource.cache

import com.abdulmateen.cmpskeleton.feature.main.home.data.database.ProductEntity
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDatabase {
    val products = MutableStateFlow<List<ProductEntity>>(emptyList())
}