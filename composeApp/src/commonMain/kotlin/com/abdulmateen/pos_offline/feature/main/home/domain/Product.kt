package com.abdulmateen.pos_offline.feature.main.home.domain
data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String,
    val isFavourite: Boolean = false
)