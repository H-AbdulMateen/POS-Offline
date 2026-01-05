package com.abdulmateen.pos_offline.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<MyAppDatabase>
}