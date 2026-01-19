package com.abdulmateen.pos_offline.data.mappers

import com.abdulmateen.pos_offline.data.database.entities.UnitEntity
import com.abdulmateen.pos_offline.domain.models.ItemUnit

fun UnitEntity.toUnit() = ItemUnit(
    unitId = unitId,
    name = name,
    symbol = symbol
)

fun ItemUnit.toUnitEntity() = UnitEntity(
    name = name,
    symbol = symbol
)