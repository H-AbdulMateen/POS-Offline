package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.feature.main.home.data.database.ProductEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.Rating
import com.abdulmateen.pos_offline.feature.main.home.data.network.dto.ProductDto
import com.abdulmateen.pos_offline.feature.main.home.data.network.dto.RatingDto

fun ProductDto.toProduct(): Product {
    return Product(
        category = category ?: "",
        description = description ?: "",
        id = id ?: 0,
        image = image ?: "",
        price = price ?: 0.0,
        rating = rating?.toRating() ?: Rating(count = 0, rate = 0.0),
        title =  title ?: ""

    )
}

fun Product.toProductEntity(): ProductEntity = ProductEntity(
    id = id,
    category = category,
    description = description,
    image = image,
    price = price,
    title = title,
    isFavourite = isFavourite
)

fun ProductEntity.toProduct(): Product = Product(
    category = category,
    description = description,
    id = id,
    image = image,
    price = price,
    rating = Rating(count = 100, rate = 4.5),
    title = title,
    isFavourite = isFavourite
)

fun RatingDto.toRating(): Rating {
    return Rating(
        count = count ?: 0,
        rate = rate ?: 0.0

    )
}