package com.abdulmateen.pos_offline.domain.use_cases

import com.abdulmateen.pos_offline.domain.use_cases.cart.AddItemToCart
import com.abdulmateen.pos_offline.domain.use_cases.cart.CalculateSubTotal
import com.abdulmateen.pos_offline.domain.use_cases.cart.ClearCartItems
import com.abdulmateen.pos_offline.domain.use_cases.cart.DecrementInQuantity
import com.abdulmateen.pos_offline.domain.use_cases.cart.GetCartItemCount
import com.abdulmateen.pos_offline.domain.use_cases.cart.GetCartItemList
import com.abdulmateen.pos_offline.domain.use_cases.cart.IncrementInQuantity
import com.abdulmateen.pos_offline.domain.use_cases.cart.RemoveItem
import com.abdulmateen.pos_offline.domain.use_cases.cart.UpdateCartItemPrice

data class CartUseCases(
    val getCartItemList: GetCartItemList,
    val getCartItemCount: GetCartItemCount,
    val addItemToCart: AddItemToCart,
    val removeItem: RemoveItem,
    val clearCartItems: ClearCartItems,
    val incrementInQuantity: IncrementInQuantity,
    val decrementInQuantity: DecrementInQuantity,
    val calculateSubTotal: CalculateSubTotal,
    val updateCartItemPrice: UpdateCartItemPrice
)
