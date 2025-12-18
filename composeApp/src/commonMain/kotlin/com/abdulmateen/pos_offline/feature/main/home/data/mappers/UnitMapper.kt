package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.feature.main.home.data.database.models.UnitEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit

fun UnitEntity.toUnit() = ItemUnit(
    unitId = unitId,
    name = name,
    symbol = symbol
)

fun ItemUnit.toUnitEntity() = UnitEntity(
    name = name,
    symbol = symbol
)