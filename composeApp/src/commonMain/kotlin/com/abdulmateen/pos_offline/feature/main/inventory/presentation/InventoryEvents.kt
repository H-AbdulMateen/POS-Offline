package com.abdulmateen.pos_offline.feature.main.inventory.presentation

sealed interface InventoryEvents {
    object NewProductSaved: InventoryEvents
    object ProductDeleted: InventoryEvents
    object ProductUpdated: InventoryEvents

}