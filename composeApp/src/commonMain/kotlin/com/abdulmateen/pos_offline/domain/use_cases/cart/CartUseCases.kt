package com.abdulmateen.pos_offline.domain.use_cases.cart

data class CartUseCases(
    val getCartItemList: GetCartItemList,
    val getCartItemCount: GetCartItemCount,
    val addItemToCart: AddItemToCart,
    val removeItem: RemoveItem,
    val clearCartItems: ClearCartItems,
    val incrementInQuantity: IncrementInQuantity,
    val decrementInQuantity: DecrementInQuantity
)
