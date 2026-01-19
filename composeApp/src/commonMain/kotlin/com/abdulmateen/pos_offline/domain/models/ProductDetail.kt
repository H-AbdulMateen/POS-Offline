package com.abdulmateen.pos_offline.domain.models

data class ProductDetail(
    val productId: Long = 0,
    val name: String,
    val description: String? = null,
    val sku: String,
    val barcode: String,
    val purchasePrice: Double,
    val price: Double,
    val discount: Double = 0.0,
    val stock: Double,
    val photoBytes: ByteArray? = null,
    val category: Category?,
    val unit: ItemUnit?,
){
    companion object {
        fun empty(): ProductDetail = ProductDetail(
            productId = 0,
            name = "",
            description = "",
            sku = "",
            barcode = "",
            purchasePrice = 0.0,
            price = 0.0,
            stock = 0.0,
            photoBytes = null,
            category = null,
            unit = null
        )
    }
}