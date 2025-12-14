package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.feature.main.home.data.database.models.UnitEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Unit

fun UnitEntity.toUnit() = Unit(
    unitId = unitId,
    name = name,
    symbol = symbol
)

fun Unit.toUnitEntity() = UnitEntity(
    name = name,
    symbol = symbol
)