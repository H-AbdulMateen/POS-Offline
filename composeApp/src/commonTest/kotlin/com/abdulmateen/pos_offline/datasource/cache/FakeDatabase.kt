package com.abdulmateen.pos_offline.datasource.cache

import com.abdulmateen.pos_offline.feature.main.home.data.database.ProductEntity
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDatabase {
    val products = MutableStateFlow<List<ProductEntity>>(emptyList())
}