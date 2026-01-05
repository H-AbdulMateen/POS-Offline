package com.abdulmateen.pos_offline.domain.models

data class ItemUnit(
    val unitId: Long = 0,
    val name: String,           // e.g. "Piece", "Kilogram"
    val symbol: String          // e.g. "pcs", "kg"
)

val dummyItemUnits = listOf(
    ItemUnit(unitId = 1, name = "Piece", symbol = "pcs"),
    ItemUnit(unitId = 2, name = "Kilogram", symbol = "kg"),
    ItemUnit(unitId = 3, name = "Liter", symbol = "L"),
    ItemUnit(unitId = 4, name = "Milliliter", symbol = "ml"),
    ItemUnit(unitId = 5, name = "Gram", symbol = "g"),
    ItemUnit(unitId = 6, name = "Pound", symbol = "lb"),
    ItemUnit(unitId = 7, name = "Ounce", symbol = "oz"),
    ItemUnit(unitId = 8, name = "Inch", symbol = "in"),
    ItemUnit(unitId = 9, name = "Foot", symbol = "ft"),
    ItemUnit(unitId = 10, name = "Yard", symbol = "yd"),
    ItemUnit(unitId = 11, name = "Mile", symbol = "mi"),
)
