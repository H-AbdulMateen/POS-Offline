package com.abdulmateen.pos_offline.feature.inventory.presentation.models

import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.Product

data class ProductUi(
    val productId: Long = 0,
    val name: String,
    val sku: String,
    val price: Double,
    val discount: Double = 0.0,
    val quantity: Double,
    val photoBytes: ByteArray? = null,
    val unit: ItemUnit? = null,
    var isOptionRevealed: Boolean = false
)

fun Product.toProductUi() =
    _root_ide_package_.com.abdulmateen.pos_offline.feature.inventory.presentation.models.ProductUi(
        productId = productId,
        name = name,
        sku = sku,
        price = price,
        discount = discount,
        quantity = quantity,
        photoBytes = photoBytes,
        unit = unit,
        isOptionRevealed = false
    )

fun com.abdulmateen.pos_offline.feature.inventory.presentation.models.ProductUi.toProduct() = Product(
    productId = productId,
    name = name,
    sku = sku,
    price = price,
    discount = discount,
    quantity = quantity,
    photoBytes = photoBytes,
    unit = unit
)

