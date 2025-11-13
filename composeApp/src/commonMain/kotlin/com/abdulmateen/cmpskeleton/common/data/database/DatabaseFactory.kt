package com.abdulmateen.cmpskeleton.common.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<MyAppDatabase>
}