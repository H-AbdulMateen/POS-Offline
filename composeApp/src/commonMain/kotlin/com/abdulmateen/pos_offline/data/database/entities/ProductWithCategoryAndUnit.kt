package com.abdulmateen.pos_offline.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.abdulmateen.pos_offline.data.database.entities.UnitEntity

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
    val unit: UnitEntity?
)