package com.abdulmateen.pos_offline.domain.use_cases.product

data class ProductUseCases(
    val searchProduct: SearchProductByName,
    val addProduct: AddProduct,
    val deleteProduct: DeleteProduct,
    val getProductList: GetProductList,
)
