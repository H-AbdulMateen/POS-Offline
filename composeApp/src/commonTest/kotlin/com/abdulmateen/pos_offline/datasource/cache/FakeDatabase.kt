package com.abdulmateen.pos_offline.datasource.cache

import kotlinx.coroutines.flow.MutableStateFlow

class FakeDatabase {
    val products = MutableStateFlow<List<ProductEntity>>(emptyList())
}