package com.abdulmateen.pos_offline.feature.main.inventory.domain.models

data class SubCategory(
    val id: Int,
    val parentCategory: Category,
    val name: String,
)
