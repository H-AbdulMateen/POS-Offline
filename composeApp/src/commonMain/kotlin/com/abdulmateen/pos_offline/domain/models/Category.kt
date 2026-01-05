package com.abdulmateen.pos_offline.domain.models

data class Category(
    val categoryId: Long = 0,
    val name: String,
    val description: String? = null
)

val dummyCategories = listOf(
    Category(categoryId = 1, name = "Fruits"),
    Category(categoryId = 2, name = "Vegetables"),
    Category(categoryId = 3, name = "Dairy"),
    Category(categoryId = 4, name = "Bakery"),
)
