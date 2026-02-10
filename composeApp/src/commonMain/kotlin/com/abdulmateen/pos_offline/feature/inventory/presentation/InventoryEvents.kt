package com.abdulmateen.pos_offline.feature.inventory.presentation

import org.jetbrains.compose.resources.StringResource

sealed interface InventoryEvents {
    data class OnSuccess(val message: StringResource):
        com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryEvents
    data class OnError(val message: StringResource):
        com.abdulmateen.pos_offline.feature.inventory.presentation.InventoryEvents
}