package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.core.data.filestorage.ImageStorage
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.ProductEntity
import com.abdulmateen.pos_offline.feature.main.home.data.database.models.ProductWithCategoryAndUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ProductDetail

suspend fun ProductWithCategoryAndUnit.toProduct(imageStorage: ImageStorage) = ProductDetail(
    productId = this.product.productId,
    name = this.product.name,
    description = this.product.description,
    sku = this.product.sku,
    barcode = this.product.barcode,
    purchasePrice = this.product.purchasePrice,
    price = this.product.salePrice,
    quantity = this.product.quantity,
    photoBytes = this.product.imagePath?.let { imageStorage.getImage(it) },
    category = this.category?.toCategory(),
    unit = this.unit?.toUnit()
)

suspend fun ProductEntity.toProduct(imageStorage: ImageStorage) = Product(
    productId = this.productId,
    name = this.name,
    sku = this.sku,
    price = this.salePrice,
    quantity = this.quantity,
    photoBytes = this.imagePath?.let { imageStorage.getImage(it) }
)
