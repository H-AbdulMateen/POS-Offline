package com.abdulmateen.pos_offline.common.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<MyAppDatabase>
}