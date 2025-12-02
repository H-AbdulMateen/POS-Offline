package com.abdulmateen.pos_offline.feature.main.inventory.domain.models

data class Category(
    val id: Int,
    val name: String,
)

val dummyCategories = listOf(
    Category(id = 1, name = "Category A"),
    Category(id = 2, name = "Category B"),
    Category(id = 3, name = "Category C"),
    Category(id = 4, name = "Category D")
)
