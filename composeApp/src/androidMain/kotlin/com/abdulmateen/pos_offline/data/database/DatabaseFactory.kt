package com.abdulmateen.pos_offline.data.database
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abdulmateen.pos_offline.data.database.MyAppDatabase

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