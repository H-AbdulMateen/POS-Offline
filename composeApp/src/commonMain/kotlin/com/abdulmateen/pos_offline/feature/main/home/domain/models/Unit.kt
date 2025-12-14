package com.abdulmateen.pos_offline.feature.main.home.domain.models

data class Unit(
    val unitId: Long = 0,
    val name: String,           // e.g. "Piece", "Kilogram"
    val symbol: String          // e.g. "pcs", "kg"
)

val dummyUnits = listOf(
    Unit(unitId = 1, name = "Piece", symbol = "pcs"),
    Unit(unitId = 2, name = "Kilogram", symbol = "kg"),
    Unit(unitId = 3, name = "Liter", symbol = "L"),
    Unit(unitId = 4, name = "Milliliter", symbol = "ml"),
    Unit(unitId = 5, name = "Gram", symbol = "g"),
    Unit(unitId = 6, name = "Pound", symbol = "lb"),
    Unit(unitId = 7, name = "Ounce", symbol = "oz"),
    Unit(unitId = 8, name = "Inch", symbol = "in"),
    Unit(unitId = 9, name = "Foot", symbol = "ft"),
    Unit(unitId = 10, name = "Yard", symbol = "yd"),
    Unit(unitId = 11, name = "Mile", symbol = "mi"),
)
