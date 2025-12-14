package com.abdulmateen.pos_offline.feature.main.home.data.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithCategoryAndUnit(
    @Embedded
    val product: ProductEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryEntity?,

    @Relation(
        parentColumn = "unitId",
        entityColumn = "unitId"
    )
    val unit: UnitEntity
)
