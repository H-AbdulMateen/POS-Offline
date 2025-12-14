package com.abdulmateen.pos_offline.feature.main.home.domain.models

data class SubCategory(
    val id: Int,
    val parentCategory: Category,
    val name: String,
)

val dummySubCategories = listOf(
    SubCategory(id = 1, parentCategory = dummyCategories[0], name = "Subcategory A"),
    SubCategory(id = 2, parentCategory = dummyCategories[1], name = "Subcategory B"),
    SubCategory(id = 3, parentCategory = dummyCategories[2], name = "Subcategory C"),
    SubCategory(id = 4, parentCategory = dummyCategories[0], name = "Subcategory D")

)
