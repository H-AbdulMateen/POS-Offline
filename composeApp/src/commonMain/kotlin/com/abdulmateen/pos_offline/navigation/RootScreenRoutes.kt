package com.abdulmateen.pos_offline.navigation

import kotlinx.serialization.Serializable

sealed interface RootScreenRoutes{
    @Serializable
    data object AuthGraph: RootScreenRoutes
    @Serializable
    data object Main: RootScreenRoutes
    @Serializable
    data class ProductDetail(val productId: Int): RootScreenRoutes

}