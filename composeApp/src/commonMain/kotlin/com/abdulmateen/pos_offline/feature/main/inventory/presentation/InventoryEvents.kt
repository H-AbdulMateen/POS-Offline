package com.abdulmateen.pos_offline.feature.main.inventory.presentation

import org.jetbrains.compose.resources.StringResource

sealed interface InventoryEvents {
    data class OnSuccess(val message: StringResource): InventoryEvents
    data class OnError(val message: StringResource): InventoryEvents
}