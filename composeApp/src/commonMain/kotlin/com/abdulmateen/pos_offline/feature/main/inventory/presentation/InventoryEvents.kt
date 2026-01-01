package com.abdulmateen.pos_offline.feature.main.inventory.presentation

sealed interface InventoryEvents {
    object NewProductSaved: InventoryEvents
    object ProductDeleted: InventoryEvents
    object ProductUpdated: InventoryEvents
    object CategoryDeleted: InventoryEvents
    object CategoryAdded: InventoryEvents
    object CategoryUpdated: InventoryEvents
    object UnitAdded: InventoryEvents
    object UnitDeleted: InventoryEvents
    object UnitUpdated: InventoryEvents

}