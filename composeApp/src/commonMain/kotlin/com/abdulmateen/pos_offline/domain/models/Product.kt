package com.abdulmateen.pos_offline.domain.models

import com.abdulmateen.pos_offline.feature.inventory.presentation.models.ProductUi

data class Product(
    val productId: Long = 0,
    val name: String,
    val sku: String,
    val price: Double,
    val discount: Double = 0.0,
    val quantity: Double,
    val imagePath: String? = null,
    val photoBytes: ByteArray? = null,
    val unit: ItemUnit? = null,
    )



val dummyProducts = listOf(
    com.abdulmateen.pos_offline.feature.inventory.presentation.models.ProductUi(
        productId = 1,
        name = "Apple",
        sku = "SKU123",
        price = 2.49,
        quantity = 10.0,
        unit = ItemUnit(
            unitId = 1,
            name = "kilogram",
            symbol = "kg"
        )
    ),
    com.abdulmateen.pos_offline.feature.inventory.presentation.models.ProductUi(
        productId = 2,
        name = "Banana",
        sku = "SKU456",
        price = 1.49,
        quantity = 15.0,
        unit = ItemUnit(
            unitId = 2,
            name = "litre",
            symbol = "l"
        )
    ),
    com.abdulmateen.pos_offline.feature.inventory.presentation.models.ProductUi(
        productId = 3,
        name = "Carrot",
        sku = "SKU789",
        price = 1.29,
        quantity = 20.0,
        unit = ItemUnit(
            unitId = 3,
            name = "piece",
            symbol = "pcs"
        )
    ),
)
