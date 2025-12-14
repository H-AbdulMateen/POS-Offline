package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.feature.main.home.data.database.models.CategoryEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category

fun CategoryEntity.toCategory() = Category(
    categoryId = categoryId,
    name = name,
    description = description
)

fun Category.toCategoryEntity() = CategoryEntity(
    name = name,
    description = description
)