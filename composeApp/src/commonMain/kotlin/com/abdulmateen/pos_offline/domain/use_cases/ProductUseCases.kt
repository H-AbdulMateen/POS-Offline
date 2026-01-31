package com.abdulmateen.pos_offline.domain.use_cases

import com.abdulmateen.pos_offline.domain.use_cases.product.AddProduct
import com.abdulmateen.pos_offline.domain.use_cases.product.DeleteProduct
import com.abdulmateen.pos_offline.domain.use_cases.product.GetProductDetail
import com.abdulmateen.pos_offline.domain.use_cases.product.GetProductList
import com.abdulmateen.pos_offline.domain.use_cases.product.GetProductUiList
import com.abdulmateen.pos_offline.domain.use_cases.product.ReduceStock
import com.abdulmateen.pos_offline.domain.use_cases.product.SearchProductByName

data class ProductUseCases(
    val searchProduct: SearchProductByName,
    val addProduct: AddProduct,
    val deleteProduct: DeleteProduct,
    val getProductList: GetProductList,
    val reduceStock: ReduceStock,
    val getProductDetail: GetProductDetail,
    val getProductUiList: GetProductUiList
)