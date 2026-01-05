package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.data.database.entities.CategoryEntity
import com.abdulmateen.pos_offline.domain.models.Category

fun CategoryEntity.toCategory() = Category(
    categoryId = categoryId,
    name = name,
    description = description
)

fun Category.toCategoryEntity() = CategoryEntity(
    name = name,
    description = description
)