package com.abdulmateen.pos_offline.common.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object MyAppDatabaseConstructor: RoomDatabaseConstructor<MyAppDatabase> {
    override fun initialize(): MyAppDatabase
}