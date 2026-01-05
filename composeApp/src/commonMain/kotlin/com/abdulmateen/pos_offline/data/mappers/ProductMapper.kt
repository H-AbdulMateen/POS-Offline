package com.abdulmateen.pos_offline.feature.main.home.data.mappers

import com.abdulmateen.pos_offline.core.data.filestorage.ImageStorage
import com.abdulmateen.pos_offline.data.database.entities.ProductEntity
import com.abdulmateen.pos_offline.data.database.entities.ProductWithCategoryAndUnit
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.ProductDetail

suspend fun ProductWithCategoryAndUnit.toProduct(imageStorage: ImageStorage) = ProductDetail(
    productId = this.product.productId,
    name = this.product.name,
    description = this.product.description,
    sku = this.product.sku,
    barcode = this.product.barcode,
    purchasePrice = this.product.purchasePrice,
    price = this.product.salePrice,
    stock = this.product.stock,
    photoBytes = this.product.imagePath?.let { imageStorage.getImage(it) },
    category = this.category?.toCategory(),
    unit = this.unit?.toUnit()
)

suspend fun ProductEntity.toProduct(imageStorage: ImageStorage) = Product(
    productId = this.productId,
    name = this.name,
    sku = this.sku,
    price = this.salePrice,
    quantity = this.stock,
    photoBytes = this.imagePath?.let { imageStorage.getImage(it) }
)
