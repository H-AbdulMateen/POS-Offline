package com.abdulmateen.pos_offline.domain.models

data class CartWithItems(
    val cart: Cart,
    val items: List<CartItem>
)
