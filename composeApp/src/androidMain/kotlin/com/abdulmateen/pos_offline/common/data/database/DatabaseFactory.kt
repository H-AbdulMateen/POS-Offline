package com.abdulmateen.pos_offline.common.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<MyAppDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(MyAppDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}