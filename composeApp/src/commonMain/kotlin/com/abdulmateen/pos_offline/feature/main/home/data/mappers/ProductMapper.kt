package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.feature.main.home.data.database.models.ProductEntity
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product

fun ProductEntity.toProduct() = Product(
    productId = productId,
    name = name,
    description = description,
    sku = sku,
    barcode = barcode,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    quantity = quantity,
    imageUrl = imageUrl,
    categoryId = categoryId,
    unitId = unitId
)

fun Product.toProductEntity() = ProductEntity(
    name = name,
    description = description,
    sku = sku,
    barcode = barcode,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    quantity = quantity,
    imageUrl = imageUrl,
    categoryId = categoryId,
    unitId = unitId
)